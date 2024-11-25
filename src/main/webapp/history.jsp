<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>위치 히스토리 목록</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <!-- 네비게이션 바 -->
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="logo">Wi-Fi Service</h1>
            <ul class="nav-links">
                <li><a href="home.jsp">Home</a></li>
                <li><a href="history.jsp">위치 History</a></li>
                <li><a href="bookmark.jsp">WiFi Bookmark</a></li>
                <li><a href="bookmark_group.jsp">Bookmark 그룹 관리</a></li>
            </ul>
        </div>
    </nav>

    <!-- 메인 콘텐츠 -->
    <main class="content">
        <h2>위치 히스토리 목록</h2>

        <!-- 히스토리 테이블 -->
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>X 좌표</th>
                    <th>Y 좌표</th>
                    <th>조회일자</th>
                    <th>비고</th>
                </tr>
                </thead>
                <tbody>
                <%
                    // SQLite 데이터베이스 연결
                    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
                    String dbUrl = "jdbc:sqlite:" + dbPath;

                    try {
                        Class.forName("org.sqlite.JDBC");
                        Connection conn = DriverManager.getConnection(dbUrl);
                        Statement stmt = conn.createStatement();

                        // search_wifi 테이블에서 데이터 조회
                        String sql = "SELECT id, lat, lnt, search_dttm FROM search_wifi ORDER BY id DESC";
                        ResultSet rs = stmt.executeQuery(sql);

                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String lat = rs.getString("lat");
                            String lnt = rs.getString("lnt");
                            String searchDttm = rs.getString("search_dttm");
                %>
                <tr>
                    <td><%= id %></td>
                    <td><%= lat %></td>
                    <td><%= lnt %></td>
                    <td><%= searchDttm %></td>
                    <td>
                        <form method="post" action="deleteHistory.jsp" style="display: inline;">
                            <input type="hidden" name="id" value="<%= id %>">
                            <button type="submit">삭제</button>
                        </form>
                    </td>
                </tr>
                <%
                    }
                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                %>
                <tr>
                    <td colspan="5" style="text-align: center; color: red;">데이터를 불러오는 중 오류가 발생했습니다.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </main>

    <!-- JavaScript 파일 불러오기 -->
    <script src="js/functions.js"></script>

</body>
</html>
