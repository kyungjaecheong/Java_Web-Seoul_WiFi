<%@ page import="bookmark.BookmarkGroupDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 클라이언트로부터 전달받은 요청 데이터의 인코딩 설정 (한글 깨짐 방지)
    request.setCharacterEncoding("UTF-8");

    // 요청 파라미터 가져오기
    String idParam = request.getParameter("id");
    String name = request.getParameter("name");
    String orderNoParam = request.getParameter("orderNo");

    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    try {
        // **유효성 검사**
        // ID가 null이거나 숫자가 아니면 예외 발생
        if (idParam == null || !idParam.matches("\\d+")) {
            throw new IllegalArgumentException("ID가 유효하지 않습니다.");
        }
        // 이름이 null이거나 공백 문자열인 경우 예외 발생
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("북마크 이름을 입력해주세요.");
        }
        // 순서가 null이거나 숫자가 아니면 예외 발생
        if (orderNoParam == null || !orderNoParam.matches("\\d+")) {
            throw new IllegalArgumentException("순서는 숫자여야 합니다.");
        }

        // **파라미터 변환**
        int id = Integer.parseInt(idParam);
        int orderNo = Integer.parseInt(orderNoParam);

        // **DB 업데이트**
        // BookmarkGroupDBTool을 사용하여 북마크 그룹 업데이트
        boolean success = BookmarkGroupDBTool.updateBookmarkGroup(dbPath, id, name.trim(), orderNo);
        if (success) {
            // 성공적으로 수정된 경우 그룹 관리 페이지로 리다이렉트
            response.sendRedirect("bookmark_group.jsp");
        } else {
            // 수정 실패 시 실패 메시지 출력
            out.println("<p style='color: red;'>수정 실패</p>");
        }
    } catch (IllegalArgumentException e) {
        // 유효성 검사에서 발생한 예외 처리: 사용자에게 입력 오류 메시지 표시
        out.println("<p style='color: red;'>입력 오류: " + e.getMessage() + "</p>");
    } catch (Exception e) {
        // 기타 예외 처리: 예외 메시지를 출력하고 로그에 기록
        e.printStackTrace(System.err);
        out.println("<p style='color: red;'>오류: " + e.getMessage() + "</p>");
    }
%>
