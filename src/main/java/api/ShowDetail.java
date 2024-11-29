package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * ShowDetail 클래스는 특정 와이파이 관리번호(mgrNo)에 대한 상세 정보를 관리 및 제공하는 클래스입니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowDetail {
    private String mgrNo;       // 관리번호
    private String wrdofc;      // 자치구
    private String mainNm;      // 와이파이명
    private String adres1;      // 도로명주소
    private String adres2;      // 상세주소
    private String instlFloor;  // 설치위치(층)
    private String instlTy;     // 설치유형
    private String instlMby;    // 설치기관
    private String svcSe;       // 서비스구분
    private String cmcwr;       // 망종류
    private String cnstcYear;   // 설치년도
    private String inoutDoor;   // 실내외구분
    private String remars3;     // WiFi접속환경
    private String lat;         // Y좌표 (위도)
    private String lnt;         // X좌표 (경도)
    private String workDttm;    // 작업일자

    /**
     * 데이터베이스에서 관리번호(mgrNo)를 기준으로 와이파이의 상세 정보를 가져옵니다.
     *
     * @param dbPath SQLite 데이터베이스 파일 경로
     * @param mgrNo 관리번호 (조회할 와이파이 ID)
     * @return ShowDetail 객체로 반환
     * @throws Exception 데이터베이스 연결, 쿼리 실행, 또는 데이터 누락 시 예외 발생
     */
    public static ShowDetail getWifiDetail(String dbPath, String mgrNo) throws Exception {
        // 데이터베이스 연결 URL 생성
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 조회할 SQL 쿼리문
        String query = "SELECT * FROM public_wifi WHERE x_swifi_mgr_no = ?";

        // 데이터베이스 연결 및 쿼리 실행
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // 쿼리 파라미터 설정
            pstmt.setString(1, mgrNo);

            // 결과를 처리하고 ShowDetail 객체로 반환
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ShowDetail(
                            rs.getString("x_swifi_mgr_no"),    // 관리번호
                            rs.getString("x_swifi_wrdofc"),    // 자치구
                            rs.getString("x_swifi_main_nm"),   // 와이파이명
                            rs.getString("x_swifi_adres1"),    // 도로명주소
                            rs.getString("x_swifi_adres2"),    // 상세주소
                            rs.getString("x_swifi_instl_floor"), // 설치위치(층)
                            rs.getString("x_swifi_instl_ty"),  // 설치유형
                            rs.getString("x_swifi_instl_mby"), // 설치기관
                            rs.getString("x_swifi_svc_se"),    // 서비스구분
                            rs.getString("x_swifi_cmcwr"),     // 망종류
                            rs.getString("x_swifi_cnstc_year"), // 설치년도
                            rs.getString("x_swifi_inout_door"), // 실내외구분
                            rs.getString("x_swifi_remars3"),   // WiFi접속환경
                            rs.getString("lat"),               // Y좌표 (위도)
                            rs.getString("lnt"),               // X좌표 (경도)
                            rs.getString("work_dttm")          // 작업일자
                    );
                }
            }
        }
        // 데이터가 없을 경우 예외 발생
        throw new Exception("No data found for MGR_NO: " + mgrNo);
    }
}
