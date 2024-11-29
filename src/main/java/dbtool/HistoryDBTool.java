package dbtool;

import java.sql.*;
import java.util.*;

/**
 * HistoryDBTool 클래스는 SQLite 데이터베이스와의 상호작용을 통해
 * 위치 히스토리 데이터를 관리하는 유틸리티 클래스를 제공합니다.
 */
public class HistoryDBTool {
    // SQLite URL 접두사
    private static final String DB_URL = "jdbc:sqlite:";

    /**
     * 위치 데이터를 데이터베이스에 삽입하는 메서드.
     *
     * @param dbPath SQLite 데이터베이스 파일 경로
     * @param lat    입력된 위도
     * @param lnt    입력된 경도
     * @return 삽입 성공 여부 (true: 성공, false: 실패)
     * @throws Exception 삽입 중 오류 발생 시 예외 발생
     */
    public static boolean insertLocation(String dbPath, String lat, String lnt) throws Exception {
        String fullDbUrl = DB_URL + dbPath;
        String insertSQL = "INSERT INTO search_wifi (lat, lnt, search_dttm) VALUES (?, ?, datetime('now'))";

        try (Connection conn = DriverManager.getConnection(fullDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, lat); // 위도 값 설정
            pstmt.setString(2, lnt); // 경도 값 설정

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;    // 삽입된 행 수가 0보다 크면 성공
        } catch (Exception e) {
            throw new Exception("Error while inserting location into the database: " + e.getMessage(), e);
        }
    }

    /**
     * 데이터베이스에서 위치 히스토리 데이터를 가져오는 메서드.
     *
     * @param dbPath SQLite 데이터베이스 파일 경로
     * @return 위치 히스토리 리스트 (List<History>)
     * @throws Exception 데이터베이스 읽기 중 오류 발생 시 예외 발생
     */
    public static List<History> getHistoryList(String dbPath) throws Exception {
        String fullDbUrl = DB_URL + dbPath;
        String querySQL = "SELECT id, lat, lnt, search_dttm FROM search_wifi ORDER BY id DESC";
        List<History> historyList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(fullDbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            while (rs.next()) {
                // ResultSet에서 데이터를 읽어 History 객체 생성 후 리스트에 추가
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
     * 데이터베이스에서 특정 ID의 위치 히스토리를 삭제하는 메서드.
     *
     * @param dbPath SQLite 데이터베이스 파일 경로
     * @param id     삭제 대상 히스토리의 ID
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     * @throws Exception 삭제 중 오류 발생 시 예외 발생
     */
    public static boolean deleteHistoryById(String dbPath, int id) throws Exception {
        String fullDbUrl = DB_URL + dbPath;
        String deleteSQL = "DELETE FROM search_wifi WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(fullDbUrl);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, id);    // 삭제 대상 ID 설정

            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0; // 삭제된 행 수가 0보다 크면 성공
        } catch (Exception e) {
            throw new Exception("Error while deleting history from the database: " + e.getMessage(), e);
        }
    }

    /**
     * 데이터베이스에서 모든 위치 히스토리를 삭제하는 메서드.
     *
     * @param dbPath SQLite 데이터베이스 파일 경로
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     * @throws SQLException SQL 실행 중 오류 발생 시 예외 발생
     */
    public static boolean deleteAllHistory(String dbPath) throws SQLException {
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 히스토리 전체 삭제 쿼리
        String deleteAllHistoryQuery = "DELETE FROM search_wifi";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false); // 트랜잭션 시작

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(deleteAllHistoryQuery);
                conn.commit(); // 성공 시 커밋
                return true;
            } catch (SQLException e) {
                conn.rollback(); // 오류 발생 시 롤백
                throw e;
            }
        }
    }

}
