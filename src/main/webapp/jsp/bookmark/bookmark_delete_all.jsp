<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // 데이터베이스 경로를 웹 애플리케이션의 실제 경로로 설정
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // BookmarkDBTool을 사용하여 모든 북마크 삭제
        boolean success = BookmarkDBTool.deleteAllBookmarks(dbPath);
        if (success) {
            // 삭제가 성공하면 북마크 목록 페이지로 리디렉션
            response.sendRedirect("bookmark.jsp");
        } else {
            // 삭제 실패 시 사용자에게 실패 메시지를 표시
            out.println("<p>전체 삭제에 실패했습니다.</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p>오류: " + e.getMessage() + "</p>");
    }
%>
