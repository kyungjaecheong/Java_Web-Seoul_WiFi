<%@ page import="bookmark.BookmarkDBTool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 요청 파라미터에서 전송된 값에서 한글 깨짐 방지를 위한 설정
    request.setCharacterEncoding("UTF-8");

    // 클라이언트로부터 전달받은 파라미터(groupId: 북마크 그룹 ID, mgrNo: 관리번호)
    String groupNoParam = request.getParameter("groupId");
    String mgrNo = request.getParameter("mgrNo");

    // 데이터베이스 경로를 절대 경로로 설정
    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

    // groupId를 정수형으로 변환
    int groupNo = Integer.parseInt(groupNoParam);
    boolean success;    // 북마크 추가 성공 여부를 저장할 변수

    try {
        // BookmarkDBTool을 사용하여 북마크 추가 작업 수행
        success = BookmarkDBTool.addBookmark(dbPath, groupNo, mgrNo);

        // 북마크 추가 성공 여부에 따라 메시지 출력
        if (success) {
    %>
    <!-- 북마크 추가 성공 메시지 -->
    <p style="color: green;">북마크가 성공적으로 추가되었습니다.</p>
    <%
    } else {
    %>
    <!-- 이미 존재하는 북마크에 대한 알림 메시지 -->
    <p style="color: red;">이미 존재하는 북마크입니다.</p>
    <%
        }
    } catch (Exception e) {
        e.printStackTrace(System.err);
    %>
    <!-- 에러 발생 시 메시지 출력 -->
    <p style="color: red;">오류 발생: <%= e.getMessage() %></p>
    <%
        }
    %>
