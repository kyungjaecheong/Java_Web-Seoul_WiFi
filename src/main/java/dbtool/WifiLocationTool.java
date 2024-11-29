package dbtool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * WifiLocationTool 클래스는 와이파이 위치 데이터를 관리하고,
 * 특정 위치로부터 가장 가까운 와이파이를 검색하는 기능을 제공합니다.
 */
public class WifiLocationTool {

    /**
     * 두 지리적 좌표 간 거리를 계산합니다 (Haversine 공식 사용).
     *
     * @param lat1 첫 번째 좌표의 위도
     * @param lon1 첫 번째 좌표의 경도
     * @param lat2 두 번째 좌표의 위도
     * @param lon2 두 번째 좌표의 경도
     * @return 거리 (단위: km)
     */
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km 단위)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 거리 반환 (단위: km)
    }

    /**
     * 입력된 위치(위도, 경도)로부터 가장 가까운 와이파이 20개 데이터를 가져옵니다.
     *
     * @param dbPath 데이터베이스 파일 경로
     * @param lat    검색 기준점의 위도
     * @param lnt    검색 기준점의 경도
     * @return 가장 가까운 20개의 WifiLocation 리스트
     * @throws Exception 데이터베이스 연결 또는 쿼리 실행 중 오류 발생 시 예외
     */
    public static List<WifiLocation> getNearestLocations(String dbPath, double lat, double lnt) throws Exception {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String querySQL = "SELECT x_swifi_mgr_no, x_swifi_wrdofc, x_swifi_main_nm, x_swifi_adres1, x_swifi_adres2, " +
                "x_swifi_instl_floor, x_swifi_instl_ty, x_swifi_instl_mby, x_swifi_svc_se, x_swifi_cmcwr, " +
                "x_swifi_cnstc_year, x_swifi_inout_door, x_swifi_remars3, lat, lnt, work_dttm FROM public_wifi";

        List<WifiLocation> wifiLocations = new ArrayList<>();

        // 데이터베이스 연결 및 데이터 조회
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                double wifiLat = rs.getDouble("lat");
                double wifiLnt = rs.getDouble("lnt");

                // 입력 좌표와의 거리 계산
                double distance = calculateDistance(lat, lnt, wifiLat, wifiLnt);

                // WifiLocation 객체 생성 후 리스트에 추가
                wifiLocations.add(new WifiLocation(
                        rs.getString("x_swifi_mgr_no"),     // 관리번호
                        rs.getString("x_swifi_wrdofc"),     // 자치구
                        rs.getString("x_swifi_main_nm"),    // 와이파이명
                        rs.getString("x_swifi_adres1"),     // 도로명 주소
                        rs.getString("x_swifi_adres2"),     // 상세 주소
                        rs.getString("x_swifi_instl_floor"), // 설치 위치(층)
                        rs.getString("x_swifi_instl_ty"),    // 설치유형
                        rs.getString("x_swifi_instl_mby"),   // 설치기관
                        rs.getString("x_swifi_svc_se"),      // 서비스구분
                        rs.getString("x_swifi_cmcwr"),       // 망종류
                        rs.getString("x_swifi_cnstc_year"),  // 설치년도
                        rs.getString("x_swifi_inout_door"),  // 실내외 구분
                        rs.getString("x_swifi_remars3"),     // WiFi 접속환경
                        wifiLat,                                         // 위도
                        wifiLnt,                                         // 경도
                        rs.getString("work_dttm"),           // 작업일자
                        distance                                         // 거리
                ));
            }
        }

        // 거리 기준으로 정렬 후 상위 20개 반환
        wifiLocations.sort(Comparator.comparingDouble(WifiLocation::getDistance));
        return wifiLocations.subList(0, Math.min(20, wifiLocations.size()));
    }
}
