<%@ page import="java.sql.*" %>
<%@ page import="java.io.File" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>서울시 공공와이파이 정보 서비스</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            color: #333;
        }
        h1 {
            color: #4CAF50;
            font-size: 2.5rem;
            margin-bottom: 1rem;
            text-align: center;
        }
        h2 {
            font-size: 1.5rem;
            margin-bottom: 2rem;
            color: #666;
        }
        p {
            font-size: 1rem;
            line-height: 1.6;
            margin-bottom: 1rem;
        }
        button {
            padding: 12px 24px;
            font-size: 1rem;
            font-weight: bold;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button:hover {
            background-color: #45a049;
        }
        .container {
            background: white;
            box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            padding: 2rem;
            width: 90%;
            max-width: 600px;
            text-align: center;
        }
        .db-info {
            text-align: left;
            margin-top: 1rem;
        }
    </style>
</head>
<body>
    <h1>서울시 공공와이파이<br>정보 열람 서비스</h1>
    <button onclick="location.href='home.jsp'">홈페이지로 이동</button>
    <br><br><br>
    <h2>데이터베이스 초기화 상태</h2>
    <%
        // 데이터베이스 파일 경로 설정
        String dbPath = request.getServletContext().getRealPath("/WEB-INF/db/wifiDatabase.db");
        String dbUrl = "jdbc:sqlite:" + dbPath;
        System.out.println("DB Path: " + dbUrl);
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(dbUrl);
            Statement stmt = conn.createStatement();

            out.println("<p>데이터베이스를 성공적으로 불러왔습니다<br></p>");
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");
            out.println("<div class='db-info'><p>데이터베이스에 다음 테이블이 존재합니다:</p>");
            while (rs.next()) {
                out.println("<p>- " + rs.getString("name") + "</p>");
            }
            out.println("</div>");

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace(System.out);
            out.println("<p style='color: red;'>데이터베이스 초기화 중 오류가 발생했습니다: "
                    + e.getMessage() + "</p>");
        }
    %>

</body>
</html>
