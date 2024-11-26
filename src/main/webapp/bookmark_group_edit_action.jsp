<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8"); // 한글 깨짐 방지

    String idParam = request.getParameter("id");
    String name = request.getParameter("name");
    String orderNoParam = request.getParameter("orderNo");
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // 유효성 검사
        if (idParam == null || !idParam.matches("\\d+")) {
            throw new IllegalArgumentException("ID가 유효하지 않습니다.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("북마크 이름을 입력해주세요.");
        }
        if (orderNoParam == null || !orderNoParam.matches("\\d+")) {
            throw new IllegalArgumentException("순서는 숫자여야 합니다.");
        }

        // 파라미터 변환
        int id = Integer.parseInt(idParam);
        int orderNo = Integer.parseInt(orderNoParam);

        // DB 업데이트
        boolean success = BookmarkGroupDBTool.updateBookmarkGroup(dbPath, id, name.trim(), orderNo);
        if (success) {
            response.sendRedirect("bookmark_group.jsp");
        } else {
            out.println("<p style='color: red;'>수정 실패</p>");
        }
    } catch (IllegalArgumentException e) {
        out.println("<p style='color: red;'>입력 오류: " + e.getMessage() + "</p>");
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    }
%>