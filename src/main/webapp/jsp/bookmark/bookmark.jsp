<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page import="bookmark.Bookmark" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>북마크 목록</title>
  <link rel="stylesheet" type="text/css" href="../../css/style.css">
</head>
<body>

<!-- 네비게이션 바 -->
<nav class="navbar">
  <div class="navbar-container">
    <h1 class="logo">Wi-Fi Service</h1>
    <ul class="nav-links">
      <li><a href="../home/home.jsp">Home</a></li>
      <li><a href="../history/history.jsp">위치 History</a></li>
      <li><a href="bookmark.jsp">WiFi Bookmark</a></li>
      <li><a href="../bookmark_group/bookmark_group.jsp">Bookmark 그룹 관리</a></li>
    </ul>
  </div>
</nav>

<!-- 메인 컨텐츠 -->
<main class="content">
  <div class="action-container">
    <h2>북마크 목록</h2>
    <!-- 전체 삭제 버튼 -->
    <form action="bookmark_delete_all.jsp" method="post" style="display: inline;">
      <button type="submit" class="delete-all-button">전체 삭제</button>
    </form>
  </div>
  <div class="table-container">
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>북마크 그룹 이름</th>
        <th>와이파이명</th>
        <th>등록일자</th>
        <th>비고</th>
      </tr>
      </thead>
      <tbody>
      <%
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        try {
          List<Bookmark> bookmarks = BookmarkDBTool.getBookmarks(dbPath);
          for (Bookmark bookmark : bookmarks) {
      %>
      <tr>
        <td><%= bookmark.getId() %></td>
        <td><%= bookmark.getBookmarkGroupName() %></td>
        <td><%= bookmark.getWifiName() %></td>
        <td><%= bookmark.getRegisterDttm() %></td>
        <td class="action-cell">
          <a href="bookmark_delete.jsp?id=<%= bookmark.getId() %>" class="delete-button">삭제</a>
        </td>
      </tr>
      <%
          }
        } catch (Exception e) {
          e.printStackTrace(System.err);
          out.println("<tr><td colspan='5' style='color: red;'>오류가 발생했습니다: " + e.getMessage() + "</td></tr>");
        }
      %>
      </tbody>
    </table>
  </div>
</main>
</body>
</html>
