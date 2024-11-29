<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // 데이터베이스에서 모든 북마크 그룹 삭제
        boolean success = BookmarkGroupDBTool.deleteAllBookmarkGroups(dbPath);
        if (success) {
            // 삭제 성공 시 북마크 그룹 관리 페이지로 리디렉션
            response.sendRedirect("bookmark_group.jsp");
        } else {
            // 삭제 실패 시 클라이언트에 실패 메시지 출력
            out.println("<p>전체 삭제에 실패했습니다.</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p>오류: " + e.getMessage() + "</p>");
    }
%>
