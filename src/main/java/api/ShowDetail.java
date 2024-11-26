package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowDetail {
    private String mgrNo;
    private String wrdofc;
    private String mainNm;
    private String adres1;
    private String adres2;
    private String instlFloor;
    private String instlTy;
    private String instlMby;
    private String svcSe;
    private String cmcwr;
    private String cnstcYear;
    private String inoutDoor;
    private String remars3;
    private String lat;
    private String lnt;
    private String workDttm;

    /**
     * 데이터베이스에서 관리번호(mgrNo)로 와이파이 상세 정보를 가져오는 메서드
     */
    public static ShowDetail getWifiDetail(String dbPath, String mgrNo) throws Exception {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "SELECT * FROM public_wifi WHERE x_swifi_mgr_no = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, mgrNo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ShowDetail(
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
                            rs.getString("lat"),
                            rs.getString("lnt"),
                            rs.getString("work_dttm")
                    );
                }
            }
        }
        throw new Exception("No data found for MGR_NO: " + mgrNo);
    }
}
