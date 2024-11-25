package dbtool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

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
}
