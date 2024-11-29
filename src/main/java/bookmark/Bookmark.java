package bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bookmark 클래스는 북마크 데이터를 나타내는 데이터 모델입니다.<br><br>
 * 필드:<br>
 * - `id`: 북마크 ID (고유 식별자)<br>
 * - `bookmarkGroupName`: 북마크가 속한 그룹 이름<br>
 * - `wifiName`: 북마크한 와이파이 이름<br>
 * - `registerDttm`: 북마크 등록 일자 및 시간<br>
 * - `mgrNo`: 와이파이 관리번호 (해당 와이파이를 고유 식별)<br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    private int id;                   // 북마크 ID
    private String bookmarkGroupName; // 북마크 그룹 이름
    private String wifiName;          // 와이파이명
    private String registerDttm;      // 등록 일자
    private String mgrNo;             // 와이파이 관리번호
}
