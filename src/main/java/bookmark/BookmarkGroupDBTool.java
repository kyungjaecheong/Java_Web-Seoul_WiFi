package bookmark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookmarkGroupDBTool {

    // 북마크 그룹 목록 가져오기
    public static List<BookmarkGroup> getBookmarkGroups(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "SELECT * FROM bookmark_group ORDER BY order_no";

        List<BookmarkGroup> groups = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // update_dttm이 null인 경우 빈 문자열로 처리
                String updateDttm = rs.getString("update_dttm") != null
                        ? rs.getString("update_dttm") : "";
                groups.add(new BookmarkGroup(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("order_no"),
                        rs.getString("register_dttm"),
                        updateDttm
                ));
            }
        }
        return groups;
    }

    // 북마크 그룹 추가
    public static boolean addBookmarkGroup(String dbPath, String name, int orderNo) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "INSERT INTO bookmark_group (name, order_no, register_dttm) VALUES (?, ?, datetime('now'))";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, orderNo);

            return pstmt.executeUpdate() > 0;
        }
    }

    // 북마크 그룹 수정
    public static boolean updateBookmarkGroup(String dbPath, int id, String name, int orderNo) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "UPDATE bookmark_group SET name = ?, order_no = ?, update_dttm = datetime('now') WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, orderNo);
            pstmt.setInt(3, id);

            return pstmt.executeUpdate() > 0;
        }
    }

    // 북마크 그룹 삭제
    public static boolean deleteBookmarkGroup(String dbPath, int id) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "DELETE FROM bookmark_group WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;
        }
    }

    // 북마크 그룹 전체 삭제
    public static boolean deleteAllBookmarkGroups(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;
        String query = "DELETE FROM bookmark_group";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {

            // 삭제된 행 수를 반환
            return stmt.executeUpdate(query) > 0;
        }
    }
}
