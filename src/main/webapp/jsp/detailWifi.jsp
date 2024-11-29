<%@ page import="api.ShowDetail" %>
<%@ page import="bookmark.BookmarkGroup" %>
<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>와이파이 상세 정보</title>
    <!-- CSS 파일 불러오기 -->
    <link rel="stylesheet" type="text/css" href="../css/popup.css">
</head>
<body>
<div class="popup-content">
    <h2>와이파이 상세 정보</h2>
    <%
        // URL 파라미터로 받은 mgrNo 값 처리
        String mgrNo = request.getParameter("mgrNo");   // 와이파이 관리번호를 가져옴
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

        // 북마크 그룹 리스트를 가져옴
        List<BookmarkGroup> groups;
        try {
            groups = BookmarkGroupDBTool.getBookmarkGroups(dbPath);
        } catch (SQLException e) {
            // DB 작업 중 오류 발생 시 런타임 예외를 발생시킴
            throw new RuntimeException(e);
        }
    %>
    <!-- 북마크 추가 폼 -->
    <div class="bookmark-add-form">
        <form action="bookmark/bookmark_add_action.jsp" method="post" target="result-frame" id="bookmark-form">
            <!-- Hidden input으로 mgrNo 전달 -->
            <input type="hidden" name="mgrNo" value="<%= mgrNo %>">
            <label for="groupId">북마크 그룹 선택 :</label>
            <select name="groupId" id="groupId" required>
                <% if (groups.isEmpty()) { %>
                <!-- 그룹이 없는 경우 선택 불가 -->
                <option value="" disabled selected>그룹이 없습니다</option>
                <% } else { %>
                <!-- 그룹 데이터를 드롭다운으로 표시 -->
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
            // 와이파이 상세 정보를 가져오는 메서드 호출
            ShowDetail wifiDetail = ShowDetail.getWifiDetail(dbPath, mgrNo);
    %>
    <!-- 와이파이 상세 정보를 두 개의 테이블로 표시 -->
    <div style="display: flex; justify-content: space-between;">
        <!-- 첫 번째 테이블: 기본 정보 -->
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

        <!-- 두 번째 테이블: 추가 정보 -->
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
            // 와이파이 상세 정보 조회 중 오류 발생 시 에러 메시지 출력
            e.printStackTrace(System.out);
            out.println("<p style='color: red;'>와이파이 상세 정보를 가져오는 중 오류가 발생했습니다: " + e.getMessage() + "</p>");
        }
    %>
    <!-- 팝업 창 닫기 버튼 -->
    <button onclick="window.close();">창 닫기</button>
</div>
</body>
</html>
