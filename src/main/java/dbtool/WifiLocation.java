package dbtool;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * WifiLocation 클래스는 와이파이 설치 위치 및 정보를 표현하기 위한 데이터 모델 클래스입니다.
 * 주어진 위치(위도, 경도)로부터 와이파이와의 거리 정보도 포함합니다.
 */
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
