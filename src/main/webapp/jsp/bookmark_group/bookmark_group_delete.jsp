<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 클라이언트로부터 전달받은 북마크 그룹 ID 처리
    String idParam = request.getParameter("id");    // 삭제할 그룹의 ID를 URL 파라미터로 받아옴
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    int id = Integer.parseInt(idParam); // ID를 정수로 변환

    try {
        // 데이터베이스에서 해당 ID의 북마크 그룹 삭제 (연결된 북마크도 함께 삭제 됨)
        boolean success = BookmarkGroupDBTool.deleteBookmarkGroup(dbPath, id);
        if (success) {
            // 삭제 성공 시 북마크 그룹 관리 페이지로 리디렉션
            response.sendRedirect("bookmark_group.jsp");
        } else {
            // 삭제 실패 시 클라이언트에 실패 메시지 출력
            out.println("<p style='color: red;'>삭제 실패</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    }
%>
