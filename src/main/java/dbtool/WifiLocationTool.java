package dbtool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WifiLocationTool {

    // 두 지리적 좌표 간 거리를 계산 (Haversine 공식 사용)
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

    // 가장 가까운 와이파이 20개 데이터를 가져오는 메서드
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
                double distance = calculateDistance(lat, lnt, wifiLat, wifiLnt); // 거리 계산

                // WifiLocation 객체 생성 후 리스트에 추가
                wifiLocations.add(new WifiLocation(
                        rs.getString("x_swifi_mgr_no"),
                        rs.getString("x_swifi_wrdofc"),
                        rs.getString("x_swifi_main_nm"),
                        rs.getString("x_swifi_adres1"),
                        rs.getString("x_swifi_adres2"),
                        rs.getString("x_swifi_instl_floor"),
                        rs.getString("x_swifi_instl_ty"),
                        rs.getString("x_swifi_instl_mby"),
                        rs.getString("x_swifi_svc_se"),
                        rs.getString("x_swifi_cmcwr"),
                        rs.getString("x_swifi_cnstc_year"),
                        rs.getString("x_swifi_inout_door"),
                        rs.getString("x_swifi_remars3"),
                        wifiLat,
                        wifiLnt,
                        rs.getString("work_dttm"),
                        distance
                ));
            }
        }

        // 거리 기준으로 정렬 후 상위 20개 반환
        wifiLocations.sort(Comparator.comparingDouble(WifiLocation::getDistance));
        return wifiLocations.subList(0, Math.min(20, wifiLocations.size()));
    }
}
