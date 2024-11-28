<%@ page import="dbtool.HistoryDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        boolean isDeleted = HistoryDBTool.deleteAllHistory(dbPath);
        if (isDeleted) {
            response.sendRedirect("history.jsp");
        } else {
            out.println("<p style='color: red;'>전체 삭제 실패: 데이터가 없습니다.</p>");
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>전체 삭제 중 오류가 발생했습니다: " + e.getMessage() + "</p>");
    }
%>
