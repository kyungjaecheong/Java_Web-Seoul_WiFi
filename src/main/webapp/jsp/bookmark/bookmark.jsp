<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page import="bookmark.Bookmark" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>북마크 목록</title>
  <!-- 스타일 파일 링크 -->
  <link rel="stylesheet" type="text/css" href="../../css/style.css">
</head>
<body>

<!-- 네비게이션 바 -->
<nav class="navbar">
  <div class="navbar-container">
    <h1 class="logo">Wi-Fi Service</h1>
    <!-- 네비게이션 링크 -->
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
    <table class="bookmark-table">
      <thead>
      <tr>
        <!-- 테이블 헤더 -->
        <th>ID</th>
        <th>북마크 그룹 이름</th>
        <th>와이파이명</th>
        <th>등록일자</th>
        <th>비고</th>
      </tr>
      </thead>
      <tbody>
      <%
        // 데이터베이스 경로 가져오기
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        try {
          // 북마크 목록을 데이터베이스에서 가져오기
          List<Bookmark> bookmarks = BookmarkDBTool.getBookmarks(dbPath);
          // 북마크 데이터를 테이블로 출력
          for (Bookmark bookmark : bookmarks) {
      %>
      <tr>
        <!-- 북마크 데이터 출력 -->
        <td><%= bookmark.getId() %></td>
        <td><%= bookmark.getBookmarkGroupName() %></td>
        <td>
          <!-- 와이파이명을 클릭하면 상세 정보 팝업 호출 -->
          <a href="#" onclick="openDetailPopup('<%= bookmark.getMgrNo() %>'); return false;">
            <%= bookmark.getWifiName() %>
          </a>
        </td>
        <td><%= bookmark.getRegisterDttm() %></td>
        <td class="action-cell">
          <!-- 삭제 버튼 -->
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
<!-- JavaScript 파일 불러오기 -->
<script src="../../js/functions.js"></script>
</body>
</html>
