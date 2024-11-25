<%@ page import="api.APIService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // APIService 초기화
    APIService.initialize(application);

    String resultMessage;
    try {
        // Open API 데이터 가져오기 및 저장
        String result = APIService.fetchAndSaveWifiData();

        if ("API_DATA_LOADED".equals(result)) {
            resultMessage = "API 데이터를 성공적으로 저장했습니다.";
        } else if ("SAMPLE_DATA_LOADED".equals(result)) {
            resultMessage = "API 서버 오류로 인해 sample 데이터를 불러왔습니다.";
        } else {
            resultMessage = "데이터 처리 중 오류가 발생했습니다.";
        }

        out.print(resultMessage);
    } catch (Exception e) {
        e.printStackTrace(System.out);
        out.print("오류 발생: " + e.getMessage());
    }
%>
