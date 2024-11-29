<%@ page import="api.APIService" %>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // APIService 초기화 (ServletContext를 사용해 DB 경로를 설정함)
    APIService.initialize(application);

    // 결과 메시지와 데이터 개수를 저장할 변수 선언
    String resultMessage;
    int recordCount = 0; // public_wifi 테이블의 데이터 개수를 저장할 변수

    try {
        // Open API 데이터를 가져오고 데이터베이스에 저장 (혹은 샘플 데이터를 복사해옴)
        String result = APIService.fetchAndSaveWifiData();

        // 데이터베이스 파일 경로 및 URL 설정
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // public_wifi 테이블의 데이터 개수를 가져오는 SQL 쿼리
        String countQuery = "SELECT COUNT(*) FROM public_wifi";

        // DB 연결 및 데이터 개수 확인
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countQuery)) {

            if (rs.next()) {
                recordCount = rs.getInt(1); // 테이블의 데이터 개수 저장
            }
        }

        // API 결과에 따라 결과 메시지 설정
        if ("API_DATA_LOADED".equals(result)) {
            resultMessage = "API 데이터를 성공적으로 저장했습니다.";
        } else if ("SAMPLE_DATA_LOADED".equals(result)) {
            resultMessage = "API 서버 오류로 인해 sample 데이터를 불러왔습니다.";
        } else {
            resultMessage = "데이터 처리 중 오류가 발생했습니다.";
        }

        // 결과 메시지와 데이터 개수 출력
        out.print(resultMessage + " (" + recordCount + "개)");
    } catch (Exception e) {
        // 예외 발생 시 오류 메시지 출력
        e.printStackTrace(System.out);
        out.print("오류 발생: " + e.getMessage());
    }
%>
