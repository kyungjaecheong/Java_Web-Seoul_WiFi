package bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 북마크 그룹 데이터 클래스
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkGroup {

    private int id;                // ID
    private String name;           // 북마크 이름
    private int orderNo;           // 순서
    private String registerDttm;   // 등록일자
    private String updateDttm;     // 수정일자

}
