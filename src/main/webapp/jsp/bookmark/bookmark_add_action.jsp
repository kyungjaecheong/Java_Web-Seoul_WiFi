<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

    String groupNoParam = request.getParameter("groupId");
    String mgrNo = request.getParameter("mgrNo");
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    int groupNo = Integer.parseInt(groupNoParam);
    boolean success;

    try {
        success = BookmarkDBTool.addBookmark(dbPath, groupNo, mgrNo);
        if (success) {
    %>
    <p style="color: green;">북마크가 성공적으로 추가되었습니다.</p>
    <%
    } else {
    %>
    <p style="color: red;">이미 존재하는 북마크입니다.</p>
    <%
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
    %>
    <p style="color: red;">오류 발생: <%= e.getMessage() %></p>
    <%
        }
    %>
