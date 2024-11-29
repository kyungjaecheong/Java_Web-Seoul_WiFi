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

/**
 * APIService 클래스는 OpenAPI를 통해 데이터를 가져와 데이터베이스에 저장하고,
 * 데이터 처리 및 관리 작업을 수행하는 유틸리티 클래스입니다.
 */
public class APIService {
    private static final String apiUrl = "http://openapi.seoul.go.kr:8088/696474764564616e39307548544a75/json/TbPublicWifiInfo/";
    private static String dbPath;
    private static final String SAMPLE_DB_PATH = "src/main/webapp/WEB-INF/db/sample.db";

    /**
     * ServletContext를 통해 데이터베이스 경로를 초기화합니다.
     *
     * @param context ServletContext 객체
     */
    public static void initialize(ServletContext context) {
        dbPath = context.getRealPath("/WEB-INF/db/wifiDatabase.db");
    }

    /**
     * OpenAPI 상태를 확인하여 성공 여부를 반환합니다.
     *
     * @return 상태 코드 ("INFO-000"이 성공 상태)
     * @throws Exception 요청 실패 시 발생
     */
    public static String fetchAPIStatus() throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = apiUrl + "1/1";    // 최소 범위로 API 상태 확인
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

    /**
     * OpenAPI로부터 총 데이터 수를 가져옵니다.
     *
     * @return 총 데이터 수
     * @throws Exception 요청 실패 시 발생
     */
    private static int fetchTotalCount() throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = apiUrl + "1/1"; // 최소 범위로 API 상태 확인
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

    /**
     * OpenAPI로부터 와이파이 데이터를 특정 범위 내에서 가져옵니다.
     *
     * @param start 시작 인덱스
     * @param end   종료 인덱스
     * @return JsonObject 목록
     * @throws Exception 요청 실패 시 발생
     */
    public static List<JsonObject> fetchWifiData(int start, int end) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = apiUrl + start + "/" + end;    // 범위에 따른 데이터 요청
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

    /**
     * 와이파이 데이터를 데이터베이스에 저장합니다.
     *
     * @param wifiList 저장할 JsonObject 목록
     * @throws Exception 저장 실패 시 발생
     */
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

            conn.setAutoCommit(false);  // 트랜잭션 시작

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

            pstmt.executeBatch();   // 배치 실행
            conn.commit();        // 트랜잭션 커밋
        }
    }

    /**
     * 데이터베이스의 public_wifi 테이블을 초기화합니다.
     *
     * @param dbUrl 데이터베이스 URL
     * @throws Exception 초기화 실패 시 발생
     */
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

    /**
     * OpenAPI에서 데이터를 가져오거나 sample.db 데이터를 복사하여 데이터베이스를 초기화합니다.
     *
     * @return 처리 결과 메시지
     */
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
            int totalCount = fetchTotalCount(); // 총 데이터 수 확인
            System.out.println("Total Data: " + totalCount);

            // 데이터 요청 및 저장
            int start = 1;
            int batchSize = 1000;
            while (start <= totalCount) {
                int end = Math.min(start + batchSize - 1, totalCount);
                List<JsonObject> wifiData = fetchWifiData(start, end);
                saveWifiDataToDatabase(wifiData);   // 데이터 저장
                System.out.println(start + " ~ " + end + " Data Stored");
                start += batchSize;
            }

            return "API_DATA_LOADED";
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return "ERROR";
        }
    }

    /**
     * sample.db에서 데이터를 복사하여 데이터베이스를 초기화합니다.
     *
     * @throws Exception 복사 실패 시 발생
     */
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
