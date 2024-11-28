package bookmark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDBTool {

    // 북마크 추가 (중복 방지)
    public static boolean addBookmark(String dbPath, int groupNo, String mgrNo) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 중복 확인 쿼리
        String checkDuplicateQuery = "SELECT COUNT(*) FROM bookmark_list WHERE group_no = ? AND mgr_no = ?";

        // 북마크 추가 쿼리
        String insertBookmarkQuery = "INSERT INTO bookmark_list (group_no, mgr_no, register_dttm) VALUES (?, ?, datetime('now'))";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // 중복 확인
            try (PreparedStatement checkStmt = conn.prepareStatement(checkDuplicateQuery)) {
                checkStmt.setInt(1, groupNo);
                checkStmt.setString(2, mgrNo);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // 중복 데이터 존재
                        return false;
                    }
                }
            }

            // 중복이 아닌 경우 북마크 추가
            try (PreparedStatement insertStmt = conn.prepareStatement(insertBookmarkQuery)) {
                insertStmt.setInt(1, groupNo);
                insertStmt.setString(2, mgrNo);
                return insertStmt.executeUpdate() > 0;
            }
        }
    }



    // 북마크 목록 가져오기
    public static List<Bookmark> getBookmarks(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query =
            "SELECT b.id, bg.name AS bookmark_group_name, w.x_swifi_main_nm AS wifi_name, b.register_dttm, b.mgr_no "
            + "FROM bookmark_list b "
            + "JOIN bookmark_group bg ON b.group_no = bg.id "
            + "JOIN public_wifi w ON b.mgr_no = w.x_swifi_mgr_no "
            + "ORDER BY b.id DESC"
        ;

        List<Bookmark> bookmarks = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                bookmarks.add(new Bookmark(
                        rs.getInt("id"),
                        rs.getString("bookmark_group_name"),
                        rs.getString("wifi_name"),
                        rs.getString("register_dttm"),
                        rs.getString("mgr_no") // 추가된 mgrNo 필드
                ));
            }
        }
        return bookmarks;
    }

    public static Bookmark getBookmarkById(String dbPath, int id) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "SELECT b.id, g.name AS groupName, w.x_swifi_main_nm AS wifiName, b.register_dttm, b.mgr_no " +
                "FROM bookmark_list b " +
                "JOIN bookmark_group g ON b.group_no = g.id " +
                "JOIN public_wifi w ON b.mgr_no = w.x_swifi_mgr_no " +
                "WHERE b.id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Bookmark(
                            rs.getInt("id"),
                            rs.getString("groupName"),
                            rs.getString("wifiName"),
                            rs.getString("register_dttm"),
                            rs.getString("mgr_no") // 추가된 mgrNo 필드
                    );
                }
            }
        }
        throw new SQLException("No bookmark found with ID: " + id);
    }

    // 북마크 삭제
    public static boolean deleteBookmark(String dbPath, int id) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "DELETE FROM bookmark_list WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean deleteAllBookmarks(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 북마크 전체 삭제 쿼리
        String deleteAllBookmarksQuery = "DELETE FROM bookmark_list";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (Statement stmt = conn.createStatement()) {
                int rowsDeleted = stmt.executeUpdate(deleteAllBookmarksQuery);
                conn.commit(); // 성공 시 커밋
                return true;
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                throw e;
            }
        }
    }

}
