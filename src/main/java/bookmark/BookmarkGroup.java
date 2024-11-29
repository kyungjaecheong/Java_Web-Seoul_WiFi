package bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 북마크 그룹 데이터를 저장하는 클래스입니다.<br><br>
 * 이 클래스는 북마크 그룹의 정보(이름, 순서, 등록일자, 수정일자 등)를 나타냅니다.<br><br>
 * Lombok을 사용하여 간단히 데이터 클래스를 정의합니다.
 */
@Data // Getter, Setter, toString, equals, hashCode 자동 생성
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자 생성
@NoArgsConstructor // 기본 생성자 생성
public class BookmarkGroup {

    private int id;                // 북마크 그룹 ID
    private String name;           // 북마크 그룹 이름
    private int orderNo;           // 표시 순서
    private String registerDttm;   // 등록일자 (생성된 시간)
    private String updateDttm;     // 수정일자 (마지막 업데이트된 시간)

}
