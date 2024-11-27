<%@ page import="api.ShowDetail" %>
<%@ page import="bookmark.BookmarkGroup" %>
<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 상세 정보</title>
    <link rel="stylesheet" type="text/css" href="css/popup.css">
</head>
<body>
<div class="popup-content">
    <h2>와이파이 상세 정보</h2>
    <%
        // URL 파라미터로 받은 mgrNo 값 처리
        String mgrNo = request.getParameter("mgrNo");
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

        List<BookmarkGroup> groups;
        try {
            groups = BookmarkGroupDBTool.getBookmarkGroups(dbPath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    %>
    <div class="bookmark-add-form">
        <form action="bookmark_add_action.jsp" method="post" target="result-frame" id="bookmark-form">
            <input type="hidden" name="mgrNo" value="<%= mgrNo %>">
            <label for="groupId">북마크 그룹 선택 :</label>
            <select name="groupId" id="groupId" required>
                <% if (groups.isEmpty()) { %>
                <option value="" disabled selected>그룹이 없습니다</option>
                <% } else { %>
                <% for (BookmarkGroup group : groups) { %>
                <option value="<%= group.getId() %>"><%= group.getName() %></option>
                <% } %>
                <% } %>
            </select>
            <button type="submit">북마크 추가하기</button>
        </form>
    </div>
    <!-- 결과 표시를 위한 iframe -->
    <iframe name="result-frame" id="result-frame"></iframe>

    <%
        try {
            // ShowDetail 클래스의 getWifiDetail 메서드 호출
            ShowDetail wifiDetail = ShowDetail.getWifiDetail(dbPath, mgrNo);
    %>
    <!-- 두 개의 테이블로 나누어 렌더링 -->
    <div style="display: flex; justify-content: space-between;">
        <!-- 첫 번째 테이블 -->
        <table>
            <tr><th>관리번호</th><td><%= wifiDetail.getMgrNo() %></td></tr>
            <tr><th>자치구</th><td><%= wifiDetail.getWrdofc() %></td></tr>
            <tr><th>와이파이명</th><td><%= wifiDetail.getMainNm() %></td></tr>
            <tr><th>도로명주소</th><td><%= wifiDetail.getAdres1() %></td></tr>
            <tr><th>상세주소</th><td><%= wifiDetail.getAdres2() %></td></tr>
            <tr><th>설치위치(층)</th><td><%= wifiDetail.getInstlFloor() %></td></tr>
            <tr><th>설치유형</th><td><%= wifiDetail.getInstlTy() %></td></tr>
            <tr><th>설치기관</th><td><%= wifiDetail.getInstlMby() %></td></tr>
        </table>

        <!-- 두 번째 테이블 -->
        <table>
            <tr><th>서비스구분</th><td><%= wifiDetail.getSvcSe() %></td></tr>
            <tr><th>망종류</th><td><%= wifiDetail.getCmcwr() %></td></tr>
            <tr><th>설치년도</th><td><%= wifiDetail.getCnstcYear() %></td></tr>
            <tr><th>실내외구분</th><td><%= wifiDetail.getInoutDoor() %></td></tr>
            <tr><th>WiFi접속환경</th><td><%= wifiDetail.getRemars3() %></td></tr>
            <tr><th>위도(Y)</th><td><%= wifiDetail.getLat() %></td></tr>
            <tr><th>경도(X)</th><td><%= wifiDetail.getLnt() %></td></tr>
            <tr><th>작업일자</th><td><%= wifiDetail.getWorkDttm() %></td></tr>
        </table>
    </div>
    <%
        } catch (Exception e) {
            e.printStackTrace(System.out);
            out.println("<p style='color: red;'>와이파이 상세 정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage() + "</p>");
        }
    %>
    <button onclick="window.close();">창 닫기</button>
</div>
</body>
</html>
