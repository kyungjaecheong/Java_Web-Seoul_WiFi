<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 데이터베이스 초기화 확인 (로깅 용도)
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
    String dbUrl = "jdbc:sqlite:" + dbPath;
    try {
        Class.forName("org.sqlite.JDBC"); // 드라이버 로드
        Connection conn = DriverManager.getConnection(dbUrl); // DB 연결 테스트
        conn.close();
        System.out.println("Database connected successfully!");
    } catch (Exception e) {
        System.err.println("Database connection failed: " + e.getMessage());
        e.printStackTrace(System.err);
    }
    // 이후 Home 페이지로 리다이렉트
    response.sendRedirect("jsp/home/home.jsp");
%>
