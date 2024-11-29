<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page import="bookmark.Bookmark" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>북마크 삭제</title>
    <!-- CSS 파일 연결 -->
    <link rel="stylesheet" type="text/css" href="../../css/style.css">
</head>
<body>
<!-- 네비게이션 바 -->
<nav class="navbar">
    <div class="navbar-container">
        <h1 class="logo">Wi-Fi Service</h1>
        <ul class="nav-links">
            <!-- 각 네비게이션 링크 -->
            <li><a href="../home/home.jsp">Home</a></li>
            <li><a href="../history/history.jsp">위치 History</a></li>
            <li><a href="bookmark.jsp">WiFi Bookmark</a></li>
            <li><a href="../bookmark_group/bookmark_group.jsp">Bookmark 그룹 관리</a></li>
        </ul>
    </div>
</nav>

<main class="content">
    <%
        // 클라이언트로부터 전달된 북마크 ID를 가져옴
        String idParam = request.getParameter("id");
        // 데이터베이스 경로 설정
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        // ID를 정수형으로 변환
        int id = Integer.parseInt(idParam);

        try {
            // BookmarkDBTool을 사용하여 ID에 해당하는 북마크 데이터를 가져옴
            Bookmark bookmark = BookmarkDBTool.getBookmarkById(dbPath, id);
    %>
    <!-- 삭제 확인 UI -->
    <div class="bookmark-delete-container">
        <h2>북마크 삭제</h2>
        <p>정말로 아래 북마크를 삭제하시겠습니까?</p>
        <table class="bookmark-info-table">
            <tr>
                <th>북마크 이름</th>
                <td><%= bookmark.getBookmarkGroupName() %></td>
            </tr>
            <tr>
                <th>와이파이명</th>
                <td><%= bookmark.getWifiName() %></td>
            </tr>
            <tr>
                <th>등록일자</th>
                <td><%= bookmark.getRegisterDttm() %></td>
            </tr>
        </table>
        <!-- 삭제 확인 폼 -->
        <form action="bookmark_delete_action.jsp" method="post" class="delete-form">
            <!-- 삭제할 북마크의 ID를 히든 필드로 전달 -->
            <input type="hidden" name="id" value="<%= bookmark.getId() %>">
            <button type="submit" class="delete-confirm-button">삭제</button>
            <!-- 취소 버튼: 북마크 페이지로 이동 -->
            <a href="bookmark.jsp" class="back-link">취소</a>
        </form>
    </div>
    <%
        } catch (Exception e) {
            e.printStackTrace(System.err);
            out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
        }
    %>
</main>
</body>
</html>
