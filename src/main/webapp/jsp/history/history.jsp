<%@ page import="dbtool.History" %>
<%@ page import="dbtool.HistoryDBTool" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>위치 히스토리 목록</title>
    <link rel="stylesheet" type="text/css" href="../../css/style.css">
</head>
<body>
    <!-- 네비게이션 바 -->
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="logo">Wi-Fi Service</h1>
            <ul class="nav-links">
                <li><a href="../home/home.jsp">Home</a></li>
                <li><a href="history.jsp">위치 History</a></li>
                <li><a href="../bookmark/bookmark.jsp">WiFi Bookmark</a></li>
                <li><a href="../bookmark_group/bookmark_group.jsp">Bookmark 그룹 관리</a></li>
            </ul>
        </div>
    </nav>

    <!-- 메인 콘텐츠 -->
    <main class="content">

        <!-- 전체 삭제 버튼 -->
        <div class="action-container">
            <h2>위치 히스토리 목록</h2>
            <form action="delete_all_history.jsp" method="post" style="display: inline;">
                <button type="submit" class="delete-all-button">전체 삭제</button>
            </form>
        </div>

        <!-- 히스토리 테이블 -->
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Y 좌표(위도)</th>
                    <th>X 좌표(경도)</th>
                    <th>조회일자</th>
                    <th>비고</th>
                </tr>
                </thead>
                <tbody>
                <%
                    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
                    try {
                        List<History> historyList = HistoryDBTool.getHistoryList(dbPath);
                        for (History history : historyList) {
                %>
                <tr>
                    <td><%= history.getId() %></td>
                    <td><%= history.getLat() %></td>
                    <td><%= history.getLnt() %></td>
                    <td><%= history.getSearchDttm() %></td>
                    <td class="action-cell">
                        <form method="post" action="deleteHistory.jsp" style="display: inline;">
                            <a href="deleteHistory.jsp?id=<%= history.getId() %>" class="delete-button">삭제</a>
                        </form>
                    </td>
                </tr>
                <%
                    }
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
    <script src="../../js/functions.js"></script>

</body>
</html>
