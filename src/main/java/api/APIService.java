package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.ServletContext;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class APIService {
    private static final String apiUrl = "http://openapi.seoul.go.kr:8088/696474764564616e39307548544a75/json/TbPublicWifiInfo/";
    private static String dbPath;
    private static final String SAMPLE_DB_PATH = "src/main/webapp/WEB-INF/db/sample.db";

    // ServletContext로 경로 설정
    public static void initialize(ServletContext context) {
        dbPath = context.getRealPath("/WEB-INF/db/wifiDatabase.db");
    }

    // API 상태 확인
    public static String fetchAPIStatus() throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = apiUrl + "1/1";
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonString = response.body().string();
                JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonObject tbPublicWifiInfo = root.getAsJsonObject("TbPublicWifiInfo");
                return tbPublicWifiInfo.getAsJsonObject("RESULT").get("CODE").getAsString();
            } else {
                return "ERROR";
            }
        }
    }

    // Open API로부터 총 데이터 수를 가져오는 메서드
    private static int fetchTotalCount() throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = apiUrl + "1/1"; // 첫 번째 요청에서 총 데이터 수를 가져옴
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonString = response.body().string();
                JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonObject tbPublicWifiInfo = root.getAsJsonObject("TbPublicWifiInfo");

                String resultCode = tbPublicWifiInfo.getAsJsonObject("RESULT").get("CODE").getAsString();
                if (!"INFO-000".equals(resultCode)) {
                    throw new Exception("API request failed: code "
                            + resultCode);
                }

                // 총 데이터 수 반환
                return tbPublicWifiInfo.get("list_total_count").getAsInt();
            } else {
                throw new Exception("API request failed: HTTP code "
                        + response.code());
            }
        }
    }

    // API 데이터 가져오기
    public static List<JsonObject> fetchWifiData(int start, int end) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = apiUrl + start + "/" + end;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonString = response.body().string();
                JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonObject tbPublicWifiInfo = root.getAsJsonObject("TbPublicWifiInfo");

                String resultCode = tbPublicWifiInfo.getAsJsonObject("RESULT").get("CODE").getAsString();
                if (!"INFO-000".equals(resultCode)) {
                    throw new Exception("API request failed: code "
                            + resultCode);
                }

                JsonArray rows = tbPublicWifiInfo.getAsJsonArray("row");
                List<JsonObject> wifiList = new ArrayList<>();
                for (JsonElement row : rows) {
                    wifiList.add(row.getAsJsonObject());
                }
                return wifiList;
            } else {
                throw new Exception("API request failed: HTTP code "
                        + response.code());
            }
        }
    }

    // 데이터베이스에 데이터 저장
    public static void saveWifiDataToDatabase(List<JsonObject> wifiList) throws Exception {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        String insertSQL = "INSERT INTO public_wifi ("
                + "x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, "
                + "x_swifi_adres2, x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, "
                + "x_swifi_svc_se, x_swifi_cmcwr, x_swifi_cnstc_year, x_swifi_inout_door, "
                + "x_swifi_remars3, lat, lnt, work_dttm"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false);

            for (JsonObject wifi : wifiList) {
                pstmt.setString(1, wifi.get("X_SWIFI_MGR_NO").getAsString());
                pstmt.setString(2, wifi.get("X_SWIFI_WRDOFC").getAsString());
                pstmt.setString(3, wifi.get("X_SWIFI_MAIN_NM").getAsString());
                pstmt.setString(4, wifi.get("X_SWIFI_ADRES1").getAsString());
                pstmt.setString(5, wifi.get("X_SWIFI_ADRES2").getAsString());
                pstmt.setString(6, wifi.get("X_SWIFI_INSTL_FLOOR").getAsString());
                pstmt.setString(7, wifi.get("X_SWIFI_INSTL_TY").getAsString());
                pstmt.setString(8, wifi.get("X_SWIFI_INSTL_MBY").getAsString());
                pstmt.setString(9, wifi.get("X_SWIFI_SVC_SE").getAsString());
                pstmt.setString(10, wifi.get("X_SWIFI_CMCWR").getAsString());
                pstmt.setString(11, wifi.get("X_SWIFI_CNSTC_YEAR").getAsString());
                pstmt.setString(12, wifi.get("X_SWIFI_INOUT_DOOR").getAsString());
                pstmt.setString(13, wifi.get("X_SWIFI_REMARS3").getAsString());
                pstmt.setString(14, wifi.get("LAT").getAsString());
                pstmt.setString(15, wifi.get("LNT").getAsString());
                pstmt.setString(16, wifi.get("WORK_DTTM").getAsString());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();
        }
    }

    // 데이터베이스 테이블 초기화
    private static void truncateTable(String dbUrl) throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            // 테이블 데이터 삭제
            stmt.executeUpdate("DELETE FROM public_wifi;");

            // ID 초기화 (sqlite_sequence 초기화)
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='public_wifi';");

            System.out.println("Table 'public_wifi' truncated and ID reset.");
        }
    }

    // API 데이터를 가져와 데이터베이스에 저장 (혹은 sample.db에서 데이터를 복사)
    public static String fetchAndSaveWifiData() {
        try {
            // API 상태 확인
            String status = fetchAPIStatus();
            String dbUrl = "jdbc:sqlite:" + dbPath;

            // 테이블 초기화 및 ID 리셋
            truncateTable(dbUrl);

            if (!"INFO-000".equals(status)) {
                System.err.println("API status error: " + status + ". Loading data from sample.db.");
                copyDataFromSampleDB(); // sample.db 데이터 복사
                return "SAMPLE_DATA_LOADED";
            }

            // 총 데이터 수 확인
            int totalCount = fetchTotalCount();
            System.out.println("Total Data: " + totalCount);

            // 데이터 요청 및 저장
            int start = 1;
            int batchSize = 1000;
            while (start <= totalCount) {
                int end = Math.min(start + batchSize - 1, totalCount);
                List<JsonObject> wifiData = fetchWifiData(start, end);
                saveWifiDataToDatabase(wifiData);
                System.out.println(start + " ~ " + end + " Data Stored");
                start += batchSize;
            }

            return "API_DATA_LOADED";
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return "ERROR";
        }
    }

    // sample.db에서 public_wifi 데이터를 트랜잭션 방식으로 복사
    public static void copyDataFromSampleDB() throws Exception {
        File sampleDbFile = new File(SAMPLE_DB_PATH);
        if (!sampleDbFile.exists()) {
            throw new Exception("sample.db not Found: " + sampleDbFile.getAbsolutePath());
        }

        try (Connection targetConn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Connection sourceConn = DriverManager.getConnection("jdbc:sqlite:" + SAMPLE_DB_PATH)) {

            // 트랜잭션 시작
            targetConn.setAutoCommit(false);

            // 테이블 존재 여부 확인
            try (Statement checkStmt = sourceConn.createStatement();
                 ResultSet rs = checkStmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='public_wifi';")) {
                if (!rs.next()) {
                    throw new Exception("'public_wifi' in sample.db not found");
                }
            }

            // 데이터 복사
            String selectSQL = "SELECT x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, "
                    + "x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, "
                    + "x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm FROM public_wifi;";
            String insertSQL = "INSERT INTO public_wifi (x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, "
                    + "x_swifi_adres2, x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, "
                    + "x_swifi_cmcwr, x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            try (PreparedStatement pstmt = targetConn.prepareStatement(insertSQL);
                 Statement sourceStmt = sourceConn.createStatement();
                 ResultSet rs = sourceStmt.executeQuery(selectSQL)) {

                while (rs.next()) {
                    pstmt.setString(1, rs.getString("x_swifi_mgr_no"));
                    pstmt.setString(2, rs.getString("x_swifi_wrdofc"));
                    pstmt.setString(3, rs.getString("x_swifi_main_nm"));
                    pstmt.setString(4, rs.getString("x_swifi_adres1"));
                    pstmt.setString(5, rs.getString("x_swifi_adres2"));
                    pstmt.setString(6, rs.getString("x_swifi_instl_floor"));
                    pstmt.setString(7, rs.getString("x_swifi_instl_ty"));
                    pstmt.setString(8, rs.getString("x_swifi_instl_mby"));
                    pstmt.setString(9, rs.getString("x_swifi_svc_se"));
                    pstmt.setString(10, rs.getString("x_swifi_cmcwr"));
                    pstmt.setString(11, rs.getString("x_swifi_cnstc_year"));
                    pstmt.setString(12, rs.getString("x_swifi_inout_door"));
                    pstmt.setString(13, rs.getString("x_swifi_remars3"));
                    pstmt.setString(14, rs.getString("lat"));
                    pstmt.setString(15, rs.getString("lnt"));
                    pstmt.setString(16, rs.getString("work_dttm"));
                    pstmt.addBatch();
                }

                // 배치 실행
                pstmt.executeBatch();
            }

            // 트랜잭션 커밋
            targetConn.commit();
            System.out.println("sample.db Copy Completed");

        } catch (Exception e) {
            throw new Exception("sample.db Copy Error: "
                    + e.getMessage());
        }
    }




}
