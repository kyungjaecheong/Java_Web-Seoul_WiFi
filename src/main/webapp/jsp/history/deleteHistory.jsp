<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dbtool.HistoryDBTool" %>
<!DOCTYPE html>
<html>
<head>
    <title>히스토리 삭제</title>
</head>
<body>
    <%
        // 요청(POST) 파라미터로 전달된 ID 값 가져오기
        String idParam = request.getParameter("id");
        // 데이터베이스 파일 경로 설정
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

        try {
            // 전달된 ID를 정수형으로 변환
            int id = Integer.parseInt(idParam);
            // ID에 해당하는 히스토리 데이터를 삭제
            boolean isDeleted = HistoryDBTool.deleteHistoryById(dbPath, id);
            if (isDeleted) {
                // 삭제 성공 시 히스토리 목록 페이지로 리다이렉트
                response.sendRedirect("history.jsp");
            } else {
                // 삭제 실패 시 오류 메시지 출력
                out.println("<p style='color: red;'>삭제 실패: 해당 ID가 존재하지 않습니다.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            out.println("<p style='color: red;'>삭제 중 오류가 발생했습니다: " + e.getMessage() + "</p>");
        }
    %>
</body>
</html>
