package api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WifiInfo 클래스는 서울시 공공 와이파이 정보를 표현하기 위한 데이터 모델 클래스입니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WifiInfo {
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
}
