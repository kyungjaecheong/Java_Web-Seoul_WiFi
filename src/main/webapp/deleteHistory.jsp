<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dbtool.HistoryDBTool" %>
<!DOCTYPE html>
<html>
<head>
    <title>히스토리 삭제</title>
</head>
<body>
    <%
        String idParam = request.getParameter("id");
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        String dbUrl = "jdbc:sqlite:" + dbPath;

        try {
            int id = Integer.parseInt(idParam);
            boolean isDeleted = HistoryDBTool.deleteHistoryById(dbPath, id);
            if (isDeleted) {
                response.sendRedirect("history.jsp");
            } else {
                out.println("<p style='color: red;'>삭제 실패: 해당 ID가 존재하지 않습니다.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            out.println("<p style='color: red;'>삭제 중 오류가 발생했습니다: " + e.getMessage() + "</p>");
        }
    %>
</body>
</html>
