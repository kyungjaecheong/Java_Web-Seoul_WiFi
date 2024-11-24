<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>히스토리 삭제</title>
</head>
<body>
    <%
        String id = request.getParameter("id");
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        String dbUrl = "jdbc:sqlite:" + dbPath;

        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(dbUrl);
            Statement stmt = conn.createStatement();

            // 데이터 삭제
            String sql = "DELETE FROM search_wifi WHERE id = " + id;
            stmt.executeUpdate(sql);

            stmt.close();
            conn.close();

            // 삭제 후 위치 히스토리 목록 페이지로 리다이렉트
            response.sendRedirect("history.jsp");
        } catch (Exception e) {
            e.printStackTrace(System.err);
    %>
    <p style="color: red;">히스토리 삭제 중 오류가 발생했습니다.</p>
    <%
        }
    %>
</body>
</html>
