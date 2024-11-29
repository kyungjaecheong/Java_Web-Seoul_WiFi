<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 클라이언트로부터 전달된 삭제할 북마크의 ID를 가져옴
    String idParam = request.getParameter("id");
    // 데이터베이스 경로를 웹 애플리케이션의 실제 경로로 설정
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // ID를 정수형으로 변환
        int id = Integer.parseInt(idParam);

        // BookmarkDBTool을 사용하여 해당 ID의 북마크를 삭제
        boolean success = BookmarkDBTool.deleteBookmark(dbPath, id);

        if (success) {
            // 삭제가 성공하면 북마크 목록 페이지로 리디렉션
            response.sendRedirect("bookmark.jsp");
        } else {
            // 삭제가 실패한 경우 사용자에게 실패 메시지와 링크를 표시
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
