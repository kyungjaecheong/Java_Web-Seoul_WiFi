<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 데이터베이스 초기화 및 연결 확인
    // 애플리케이션의 데이터베이스 경로를 가져옴
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
    // SQLite 데이터베이스 URL 생성
    String dbUrl = "jdbc:sqlite:" + dbPath;

    try {
        // SQLite JDBC 드라이버 로드 (한번만 해주면 됨)
        Class.forName("org.sqlite.JDBC");

        // 데이터베이스 연결 테스트
        Connection conn = DriverManager.getConnection(dbUrl);
        conn.close();   // 연결이 성공적으로 이루어진 경우 닫기

        // 성공 로그 출력
        System.out.println("Database connected successfully!");
    } catch (Exception e) {
        // 데이터베이스 연결 실패 시 오류 메시지 출력
        System.err.println("Database connection failed: " + e.getMessage());
        e.printStackTrace(System.err);  // 스택 트레이스 출력
    }

    // home 페이지로 리다이렉트
    response.sendRedirect("jsp/home/home.jsp");
%>
