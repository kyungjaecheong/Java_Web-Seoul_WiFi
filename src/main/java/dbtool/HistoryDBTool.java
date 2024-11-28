package dbtool;

import java.sql.*;
import java.util.*;


public class HistoryDBTool {
    private static final String DB_URL = "jdbc:sqlite:"; // SQLite URL

    /**
     * 위치 데이터를 데이터베이스에 삽입
     *
     * @param dbPath SQLite DB 파일 경로
     * @param lat 위도
     * @param lnt 경도
     * @return 삽입 성공 여부
     * @throws Exception 예외 발생 시
     */
    public static boolean insertLocation(String dbPath, String lat, String lnt) throws Exception {
        String fullDbUrl = DB_URL + dbPath;
        String insertSQL = "INSERT INTO search_wifi (lat, lnt, search_dttm) VALUES (?, ?, datetime('now'))";

        try (Connection conn = DriverManager.getConnection(fullDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, lat);
            pstmt.setString(2, lnt);

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            throw new Exception("Error while inserting location into the database: " + e.getMessage(), e);
        }
    }

    /**
     * 데이터베이스에서 위치 히스토리 가져오기
     *
     * @param dbPath SQLite DB 파일 경로
     * @return 위치 히스토리 리스트
     * @throws Exception 예외 발생 시
     */
    public static List<History> getHistoryList(String dbPath) throws Exception {
        String fullDbUrl = DB_URL + dbPath;
        String querySQL = "SELECT id, lat, lnt, search_dttm FROM search_wifi ORDER BY id DESC";
        List<History> historyList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(fullDbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            while (rs.next()) {
                History history = new History(
                        rs.getInt("id"),
                        rs.getString("lat"),
                        rs.getString("lnt"),
                        rs.getString("search_dttm")
                );
                historyList.add(history);
            }
        } catch (Exception e) {
            throw new Exception("Error while fetching history from the database: " + e.getMessage(), e);
        }
        return historyList;
    }

    /**
     * 데이터베이스에서 특정 ID의 위치 히스토리 삭제
     *
     * @param dbPath SQLite DB 파일 경로
     * @param id     삭제할 히스토리의 ID
     * @return 삭제 성공 여부
     * @throws Exception 예외 발생 시
     */
    public static boolean deleteHistoryById(String dbPath, int id) throws Exception {
        String fullDbUrl = DB_URL + dbPath;
        String deleteSQL = "DELETE FROM search_wifi WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(fullDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);

            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            throw new Exception("Error while deleting history from the database: " + e.getMessage(), e);
        }
    }

    public static boolean deleteAllHistory(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 히스토리 전체 삭제 쿼리
        String deleteAllHistoryQuery = "DELETE FROM search_wifi";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (Statement stmt = conn.createStatement()) {
                int rowsDeleted = stmt.executeUpdate(deleteAllHistoryQuery);
                conn.commit(); // 성공 시 커밋
                return true;
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                throw e;
            }
        }
    }

}
