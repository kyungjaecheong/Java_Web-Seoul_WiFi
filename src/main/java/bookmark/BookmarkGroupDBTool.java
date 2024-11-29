package bookmark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BookmarkGroupDBTool<br><br>
 * 북마크 그룹 관련 데이터베이스 작업을 처리하는 클래스입니다.<br><br>
 * 북마크 그룹 추가, 수정, 삭제, 조회 등의 기능을 제공합니다.
 */
public class BookmarkGroupDBTool {

    /**
     * 데이터베이스에서 모든 북마크 그룹 목록을 가져옵니다.
     *
     * @param dbPath SQLite 데이터베이스 경로
     * @return 북마크 그룹 리스트
     * @throws SQLException SQL 예외 발생 시
     */
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

    /**
     * 데이터베이스에 새로운 북마크 그룹을 추가합니다.
     *
     * @param dbPath SQLite 데이터베이스 경로
     * @param name   북마크 그룹 이름
     * @param orderNo 그룹 표시 순서
     * @return 추가 성공 여부
     * @throws SQLException SQL 예외 발생 시
     */
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

    /**
     * 데이터베이스에 저장된 북마크 그룹을 수정합니다.
     *
     * @param dbPath SQLite 데이터베이스 경로
     * @param id     수정할 북마크 그룹의 ID
     * @param name   새로운 북마크 그룹 이름
     * @param orderNo 새로운 표시 순서
     * @return 수정 성공 여부
     * @throws SQLException SQL 예외 발생 시
     */
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

    /**
     * 특정 북마크 그룹과 관련된 북마크를 포함하여 삭제합니다.
     *
     * @param dbPath SQLite 데이터베이스 경로
     * @param id     삭제할 북마크 그룹의 ID
     * @return 삭제 성공 여부
     * @throws SQLException SQL 예외 발생 시
     */
    public static boolean deleteBookmarkGroup(String dbPath, int id) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 그룹과 관련된 북마크 삭제 쿼리
        String deleteBookmarksQuery = "DELETE FROM bookmark_list WHERE group_no = ?";

        // 그룹 삭제 쿼리
        String deleteGroupQuery = "DELETE FROM bookmark_group WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 그룹과 관련된 북마크 삭제
            try (PreparedStatement deleteBookmarksStmt = conn.prepareStatement(deleteBookmarksQuery)) {
                deleteBookmarksStmt.setInt(1, id);
                deleteBookmarksStmt.executeUpdate();
            }

            // 2. 그룹 삭제
            try (PreparedStatement deleteGroupStmt = conn.prepareStatement(deleteGroupQuery)) {
                deleteGroupStmt.setInt(1, id);
                int rowsAffected = deleteGroupStmt.executeUpdate();

                if (rowsAffected > 0) {
                    conn.commit(); // 성공 시 커밋
                    return true;
                } else {
                    conn.rollback(); // 실패 시 롤백
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                throw e;
            }
        }
    }


    /**
     * 모든 북마크 그룹과 관련된 북마크를 삭제합니다.
     *
     * @param dbPath SQLite 데이터베이스 경로
     * @return 삭제 성공 여부
     * @throws SQLException SQL 예외 발생 시
     */
    public static boolean deleteAllBookmarkGroups(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 모든 북마크 삭제 쿼리
        String deleteAllBookmarksQuery = "DELETE FROM bookmark_list";

        // 모든 그룹 삭제 쿼리
        String deleteAllGroupsQuery = "DELETE FROM bookmark_group";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (Statement stmt = conn.createStatement()) {
                // 1. 모든 북마크 삭제
                stmt.executeUpdate(deleteAllBookmarksQuery);

                // 2. 모든 그룹 삭제
                stmt.executeUpdate(deleteAllGroupsQuery);

                conn.commit(); // 성공 시 커밋
                return true;
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                throw e;
            }
        }
    }

}
