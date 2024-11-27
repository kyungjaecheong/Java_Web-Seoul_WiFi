package bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    private int id;             // 북마크 ID
    private String bookmarkGroupName; // 북마크 그룹 이름
    private String wifiName;    // 와이파이명
    private String registerDttm; // 등록 일자
}
