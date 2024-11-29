package dbtool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * History 클래스는 위치 검색 히스토리 데이터를 나타내는 엔티티 클래스입니다.
 * <br>
 * - Lombok을 사용하여 Getter, Setter, toString, EqualsAndHashCode, 기본 생성자, 전체 필드 생성자를 자동 생성합니다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {

    /**
     * 히스토리 ID (기본 키 역할).
     */
    private int id;

    /**
     * 검색 당시 입력된 위도 값.
     */
    private String lat;

    /**
     * 검색 당시 입력된 경도 값.
     */
    private String lnt;

    /**
     * 검색 수행 일시.
     */
    private String searchDttm;
}
