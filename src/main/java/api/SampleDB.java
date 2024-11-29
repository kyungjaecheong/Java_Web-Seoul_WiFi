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

/**
 * SampleDB 클래스는 서울시 공공와이파이 API 데이터를 가져와
 * SQLite 데이터베이스에 저장하는 유틸리티 클래스입니다.
 */
public class SampleDB {

    private static final String DB_PATH = "src/main/webapp/WEB-INF/db/sample.db";   // 데이터베이스 경로
    private static final String API_URL = "http://openapi.seoul.go.kr:8088/696474764564616e39307548544a75/json/TbPublicWifiInfo/";

    /**
     * 데이터베이스 초기화 - 테이블 생성 (테이블이 없으면 생성)
     *
     * @param conn SQLite 데이터베이스 연결 객체
     * @throws Exception 테이블 생성 실패 시 예외 발생
     */
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
        stmt.execute(createTableSQL);   // 테이블 생성 쿼리 실행
        stmt.close();
    }

    /**
     * 데이터베이스의 기존 데이터를 초기화 (삭제)
     *
     * @param conn SQLite 데이터베이스 연결 객체
     * @throws Exception 데이터 초기화 실패 시 예외 발생
     */
    private static void truncateTable(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            // 테이블 데이터 삭제
            stmt.execute("DELETE FROM public_wifi;");
            // AUTO_INCREMENT 초기화
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='public_wifi';");
        }
    }

    /**
     * Open API로부터 데이터를 요청하고 파싱하여 WifiInfo 객체 리스트를 반환합니다.
     *
     * @param start 데이터 시작 인덱스
     * @param end 데이터 종료 인덱스
     * @return WifiInfo 객체 리스트
     * @throws Exception API 요청 실패 또는 파싱 실패 시 예외 발생
     */
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

                // JSON 데이터를 파싱하여 WifiInfo 리스트로 변환
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

    /**
     * Open API를 통해 모든 데이터를 가져옵니다.
     *
     * @return WifiInfo 객체 리스트
     * @throws Exception API 요청 실패 시 예외 발생
     */
    private static List<WifiInfo> fetchAllWifiData() throws Exception {
        List<WifiInfo> allWifiData = new ArrayList<>();

        // 첫 번째 요청으로 list_total_count 가져오기
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + "1/1"; // 총 데이터 수 확인 요청
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

    /**
     * 데이터베이스에 데이터를 삽입합니다.
     *
     * @param conn SQLite 데이터베이스 연결 객체
     * @param wifiList 삽입할 WifiInfo 리스트
     * @throws Exception 데이터 삽입 실패 시 예외 발생
     */
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


    /**
     * 메인 메서드 - 프로그램 실행 진입점
     *
     * @param args 실행 인자
     */
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
