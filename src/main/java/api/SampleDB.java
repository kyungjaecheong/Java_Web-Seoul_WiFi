package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SampleDB {

    private static final String DB_PATH = "src/main/webapp/WEB-INF/db/sample.db";
    private static final String API_URL = "http://openapi.seoul.go.kr:8088/696474764564616e39307548544a75/json/TbPublicWifiInfo/";

    // 테이블 초기화 (없으면 생성)
    private static void initializeDatabase(Connection conn) throws Exception {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS public_wifi ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "x_swifi_mgr_no TEXT, "
                + "x_swifi_wrdofc TEXT, "
                + "x_swifi_main_nm TEXT, "
                + "x_swifi_adres1 TEXT, "
                + "x_swifi_adres2 TEXT, "
                + "x_swifi_instl_floor TEXT, "
                + "x_swifi_instl_ty TEXT, "
                + "x_swifi_instl_mby TEXT, "
                + "x_swifi_svc_se TEXT, "
                + "x_swifi_cmcwr TEXT, "
                + "x_swifi_cnstc_year TEXT, "
                + "x_swifi_inout_door TEXT, "
                + "x_swifi_remars3 TEXT, "
                + "lat TEXT, "
                + "lnt TEXT, "
                + "work_dttm TIMESTAMP"
                + ");";
        Statement stmt = conn.createStatement();
        stmt.execute(createTableSQL);
        stmt.close();
    }

    // 테이블 데이터 초기화 (TRUNCATE)
    private static void truncateTable(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM public_wifi;");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='public_wifi';");
        }
    }

    // Open API 데이터 요청 및 파싱
    private static List<WifiInfo> fetchWifiData(int start, int end) throws Exception {
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + start + "/" + end;
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonString = response.body().string();
                JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonObject tbPublicWifiInfo = root.getAsJsonObject("TbPublicWifiInfo");

                // API 결과 코드 확인
                String resultCode = tbPublicWifiInfo.getAsJsonObject("RESULT").get("CODE").getAsString();
                if (!"INFO-000".equals(resultCode)) {
                    throw new Exception("API 요청 실패: 코드 " + resultCode);
                }

                // JSON 데이터를 파싱하여 리스트 반환
                JsonArray rows = tbPublicWifiInfo.getAsJsonArray("row");
                List<WifiInfo> wifiList = new ArrayList<>();
                for (JsonElement row : rows) {
                    JsonObject wifi = row.getAsJsonObject();
                    wifiList.add(new WifiInfo(
                            wifi.get("X_SWIFI_MGR_NO").getAsString(),
                            wifi.get("X_SWIFI_WRDOFC").getAsString(),
                            wifi.get("X_SWIFI_MAIN_NM").getAsString(),
                            wifi.get("X_SWIFI_ADRES1").getAsString(),
                            wifi.get("X_SWIFI_ADRES2").getAsString(),
                            wifi.get("X_SWIFI_INSTL_FLOOR").getAsString(),
                            wifi.get("X_SWIFI_INSTL_TY").getAsString(),
                            wifi.get("X_SWIFI_INSTL_MBY").getAsString(),
                            wifi.get("X_SWIFI_SVC_SE").getAsString(),
                            wifi.get("X_SWIFI_CMCWR").getAsString(),
                            wifi.get("X_SWIFI_CNSTC_YEAR").getAsString(),
                            wifi.get("X_SWIFI_INOUT_DOOR").getAsString(),
                            wifi.get("X_SWIFI_REMARS3").getAsString(),
                            wifi.get("LAT").getAsString(),
                            wifi.get("LNT").getAsString(),
                            wifi.get("WORK_DTTM").getAsString()
                    ));
                }
                return wifiList;
            } else {
                throw new Exception("API 요청 실패: HTTP 코드 " + response.code());
            }
        }
    }

    // 전체 데이터를 가져오기
    private static List<WifiInfo> fetchAllWifiData() throws Exception {
        List<WifiInfo> allWifiData = new ArrayList<>();

        // 첫 번째 요청으로 list_total_count 가져오기
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + "1/1"; // 첫 번째 요청은 총 데이터 수를 가져오기 위한 요청
        Request request = new Request.Builder().url(url).build();

        int totalCount;

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonString = response.body().string();
                JsonObject root = JsonParser.parseString(jsonString).getAsJsonObject();
                JsonObject tbPublicWifiInfo = root.getAsJsonObject("TbPublicWifiInfo");

                // API 결과 코드 확인
                String resultCode = tbPublicWifiInfo.getAsJsonObject("RESULT").get("CODE").getAsString();
                if (!"INFO-000".equals(resultCode)) {
                    throw new Exception("API 요청 실패: 코드 " + resultCode);
                }

                // 총 데이터 수 가져오기
                totalCount = tbPublicWifiInfo.get("list_total_count").getAsInt();
                System.out.println("총 데이터 수: " + totalCount);

                // 데이터를 1000개씩 가져오기
                int start = 1;
                int batchSize = 1000;
                while (start <= totalCount) {
                    int end = Math.min(start + batchSize - 1, totalCount);
                    List<WifiInfo> batchData = fetchWifiData(start, end);
                    allWifiData.addAll(batchData);
                    start += batchSize;
                }
            } else {
                throw new Exception("API 요청 실패: HTTP 코드 " + response.code());
            }
        }

        return allWifiData;
    }

    // 데이터베이스에 데이터 삽입
    private static void insertData(Connection conn, List<WifiInfo> wifiList) throws Exception {
        // 동기화 모드 최적화 (트랜잭션 전에 실행)
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA synchronous = OFF;");
            stmt.execute("PRAGMA journal_mode = MEMORY;");
        }

        // 트랜잭션 시작
        conn.setAutoCommit(false);

        String insertSQL = "INSERT INTO public_wifi ("
                + "x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, "
                + "x_swifi_adres2, x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, "
                + "x_swifi_svc_se, x_swifi_cmcwr, x_swifi_cnstc_year, x_swifi_inout_door, "
                + "x_swifi_remars3, lat, lnt, work_dttm"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (WifiInfo wifi : wifiList) {
                pstmt.setString(1, wifi.getMgrNo());
                pstmt.setString(2, wifi.getWrdofc());
                pstmt.setString(3, wifi.getMainNm());
                pstmt.setString(4, wifi.getAdres1());
                pstmt.setString(5, wifi.getAdres2());
                pstmt.setString(6, wifi.getInstlFloor());
                pstmt.setString(7, wifi.getInstlTy());
                pstmt.setString(8, wifi.getInstlMby());
                pstmt.setString(9, wifi.getSvcSe());
                pstmt.setString(10, wifi.getCmcwr());
                pstmt.setString(11, wifi.getCnstcYear());
                pstmt.setString(12, wifi.getInoutDoor());
                pstmt.setString(13, wifi.getRemars3());
                pstmt.setString(14, wifi.getLat());
                pstmt.setString(15, wifi.getLnt());
                pstmt.setString(16, wifi.getWorkDttm());
                pstmt.addBatch();
            }

            // 배치 실행
            pstmt.executeBatch();
        }

        // 트랜잭션 커밋
        conn.commit();

        // 트랜잭션 종료
        conn.setAutoCommit(true);
    }


    // Main 메서드
    public static void main(String[] args) {
        File dbFile = new File(DB_PATH);
        boolean dbExists = dbFile.exists();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            Class.forName("org.sqlite.JDBC");

            // 데이터베이스 초기화
            initializeDatabase(conn);

            // API 데이터 가져오기
            List<WifiInfo> wifiList = fetchAllWifiData();

            // 기존 데이터 초기화
            if (dbExists) {
                truncateTable(conn);
            }

            // 데이터 삽입
            insertData(conn, wifiList);
            System.out.println("데이터가 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
