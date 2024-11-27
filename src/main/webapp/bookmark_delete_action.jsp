<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 삭제 요청 처리
    String idParam = request.getParameter("id");
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        int id = Integer.parseInt(idParam); // id를 정수로 변환

        // 북마크 삭제
        boolean success = BookmarkDBTool.deleteBookmark(dbPath, id);

        if (success) {
            // 삭제 성공 시 북마크 목록으로 리디렉션
            response.sendRedirect("bookmark.jsp");
        } else {
            // 삭제 실패 메시지 출력
            out.println("<p style='color: red;'>북마크 삭제에 실패했습니다.</p>");
            out.println("<a href='bookmark.jsp'>북마크 목록으로 돌아가기</a>");
        }
    } catch (Exception e) {
        // 예외 처리: 에러 메시지 출력
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류가 발생했습니다: " + e.getMessage() + "</p>");
        out.println("<a href='bookmark.jsp'>북마크 목록으로 돌아가기</a>");
    }
%>
