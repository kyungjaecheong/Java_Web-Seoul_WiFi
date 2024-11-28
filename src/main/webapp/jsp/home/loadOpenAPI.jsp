<%@ page import="api.APIService" %>
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // APIService 초기화
    APIService.initialize(application);

    String resultMessage;
    int recordCount = 0; // 테이블의 데이터 개수를 저장할 변수

    try {
        // Open API 데이터 가져오기 및 저장
        String result = APIService.fetchAndSaveWifiData();

        // DB 경로 설정
        String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");
        String dbUrl = "jdbc:sqlite:" + dbPath;

        // 테이블의 데이터 수를 가져오는 쿼리
        String countQuery = "SELECT COUNT(*) FROM public_wifi";

        // DB 연결 및 데이터 개수 확인
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countQuery)) {

            if (rs.next()) {
                recordCount = rs.getInt(1); // 테이블의 데이터 개수 저장
            }
        }

        if ("API_DATA_LOADED".equals(result)) {
            resultMessage = "API 데이터를 성공적으로 저장했습니다.";
        } else if ("SAMPLE_DATA_LOADED".equals(result)) {
            resultMessage = "API 서버 오류로 인해 sample 데이터를 불러왔습니다.";
        } else {
            resultMessage = "데이터 처리 중 오류가 발생했습니다.";
        }

        out.print(resultMessage + " (" + recordCount + "개)");
    } catch (Exception e) {
        e.printStackTrace(System.out);
        out.print("오류 발생: " + e.getMessage());
    }
%>
