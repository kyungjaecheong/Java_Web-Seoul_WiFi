package dbtool;

import lombok.AllArgsConstructor;
import lombok.Data;

// @Data는 모든 Getter, Setter, toString, EqualsAndHashCode를 자동 생성합니다.
// @AllArgsConstructor는 모든 필드를 매개변수로 받는 생성자를 자동 생성합니다.
@Data
@AllArgsConstructor
public class WifiLocation {
    private String mgrNo;       // 관리번호
    private String wrdofc;      // 자치구
    private String mainNm;      // 와이파이명
    private String adres1;      // 도로명 주소
    private String adres2;      // 상세 주소
    private String instlFloor;  // 설치 위치(층)
    private String instlTy;     // 설치유형
    private String instlMby;    // 설치기관
    private String svcSe;       // 서비스구분
    private String cmcwr;       // 망종류
    private String cnstcYear;   // 설치년도
    private String inoutDoor;   // 실내외 구분
    private String remars3;     // WiFi접속환경
    private double lat;         // 위도
    private double lnt;         // 경도
    private String workDttm;    // 작업일자
    private double distance;    // 입력 위치로부터의 거리
}
