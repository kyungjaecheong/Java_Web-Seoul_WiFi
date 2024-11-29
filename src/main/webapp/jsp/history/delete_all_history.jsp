<%@ page import="dbtool.HistoryDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // 데이터베이스 경로 설정
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // 히스토리 데이터 전체 삭제 호출
        boolean success = HistoryDBTool.deleteAllHistory(dbPath);
        if (success) {
            // 전체 삭제 성공 시 히스토리 목록 페이지로 리다이렉트
            response.sendRedirect("history.jsp");
        } else {
            // 전체 삭제 실패 시 사용자에게 실패 메시지 출력
            out.println("<p>전체 삭제에 실패했습니다.</p>");
        }
    } catch (Exception e) {
        // 예외 발생 시 스택 트레이스를 로그에 출력
        e.printStackTrace(System.err);
        // 사용자에게 예외 메시지 출력
        out.println("<p>오류: " + e.getMessage() + "</p>");
    }
%>
