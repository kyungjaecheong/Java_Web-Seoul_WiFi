<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 클라이언트에서 전달받은 데이터가 한글일 경우 깨짐 방지
    request.setCharacterEncoding("UTF-8");

    // 클라이언트로부터 전달받은 입력값 가져오기
    String name = request.getParameter("name");
    String orderNoParam  = request.getParameter("orderNo");
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // 입력값 유효성 검사
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("북마크 이름을 입력해주세요.");
        }
        if (orderNoParam == null || !orderNoParam.matches("\\d+")) {
            throw new IllegalArgumentException("순서는 숫자여야 합니다.");
        }

        // 순서를 정수로 변환
        int orderNo = Integer.parseInt(orderNoParam);

        // DB에 북마크 그룹 추가
        boolean success = BookmarkGroupDBTool.addBookmarkGroup(dbPath, name.trim(), orderNo);

        // 성공 여부에 따라 결과 처리
        if (success) {
            // 추가 성공 시 북마크 그룹 페이지로 리디렉션
            response.sendRedirect("bookmark_group.jsp");
        } else {
            // 추가 실패 시 메시지 출력
            out.println("<p>추가 실패</p>");
        }
    } catch (IllegalArgumentException e) {
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    } catch (Exception e) {
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    }
%>
