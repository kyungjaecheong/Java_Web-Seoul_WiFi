<%@ page import="dbtool.HistoryDBTool" %>
<%@ page import="java.sql.*" %>
<%@ page import="dbtool.WifiLocation" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>서울시 공공와이파이 정보 서비스</title>
    <!-- CSS 파일 불러오기 -->
    <link rel="stylesheet" type="text/css" href="css/style.css">

</head>
<body>
    <!-- 네비게이션 바 -->
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="logo">Wi-Fi Service</h1>
            <ul class="nav-links">
                <li><a href="home.jsp">Home</a></li>
                <li><a href="history.jsp">위치 History</a></li>
                <li><a href="bookmark.jsp">WiFi Bookmark</a></li>
                <li><a href="bookmark_group.jsp">Bookmark 그룹 관리</a></li>
            </ul>
        </div>
    </nav>

    <!-- 메인 컨텐츠 -->
    <main class="content">
        <div class="form-container">
            <!-- 타이틀 -->
            <h2>와이파이 정보 구하기</h2>
            <!-- Open API 버튼 -->
            <div class="button-container">
                <button type="button" class="open-api-button" onclick="loadOpenAPI()">Open API 정보 가져오기</button>
                <span id="loading-spinner" style="display: none;">⏳</span>
            </div>
            <p id="status-message" class=""></p>

            <!-- LAT, LNT 입력 -->
            <form id="location-form">
                <div class="input-container">
                    <label for="lat">LAT (위도):</label>
                    <input type="text" id="lat" name="lat" placeholder="예: 37.5665">
                    <label for="lnt">LNT (경도):</label>
                    <input type="text" id="lnt" name="lnt" placeholder="예: 126.9780">
                    <button type="button" onclick="getMyLocation()">내 위치 가져오기</button>
                </div>
                <!-- 근처 WiFi 정보 보기 버튼 -->
                <div class="button-container">
                    <button type="submit" name="saveLocation">근처 WiFi 정보 보기</button>
                </div>
            </form>
        </div>

        <!-- 히스토리 DB 삽입 로직 -->
        <%
            String lat = request.getParameter("lat");
            String lnt = request.getParameter("lnt");
            String saveLocation = request.getParameter("saveLocation");

            if (lat != null && lnt != null && saveLocation != null) {
                String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

                try {
                    boolean isInserted = HistoryDBTool.insertLocation(dbPath, lat, lnt);
                    if (isInserted) {
                        out.println("<p style='color: green;'>위치 정보가 성공적으로 저장되었습니다.</p>");
                    } else {
                        out.println("<p style='color: red;'>위치 정보 저장 중 오류가 발생했습니다.</p>");
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                    out.println("<p style='color: red;'>위치 정보 저장 중 오류가 발생했습니다: " + e.getMessage() + "</p>");
                }
            }
        %>

        <!-- 표 -->
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>거리(Km)</th>
                    <th>관리번호</th>
                    <th>자치구</th>
                    <th>와이파이명</th>
                    <th>도로명주소</th>
                    <th>상세주소</th>
                    <th>설치위치(층)</th>
                    <th>설치유형</th>
                    <th>설치기관</th>
                    <th>서비스구분</th>
                    <th>망종류</th>
                    <th>설치년도</th>
                    <th>실내외구분</th>
                    <th>WiFi접속환경</th>
                    <th>위도(Y)</th>
                    <th>경도(X)</th>
                    <th>작업일자</th>
                </tr>
                </thead>
                <tbody>
                <%
                    String latStr = request.getParameter("lat");
                    String lntStr = request.getParameter("lnt");
                    String dbPath = application.getRealPath("/WEB-INF/db/wifiDatabase.db");

                    if (latStr != null && lntStr != null) {
                        try {
                            double latD = Double.parseDouble(latStr);
                            double lntD = Double.parseDouble(lntStr);

                            // 가장 가까운 와이파이 데이터를 가져옴
                            List<WifiLocation> wifiLocations = dbtool.WifiLocationTool.getNearestLocations(dbPath, latD, lntD);

                            for (dbtool.WifiLocation wifi : wifiLocations) {
                %>
                <tr>
                    <td><%= String.format("%.4f", wifi.getDistance()) %></td>
                    <td><%= wifi.getMgrNo() %></td>
                    <td><%= wifi.getWrdofc() %></td>
                    <td><%= wifi.getMainNm() %></td>
                    <td><%= wifi.getAdres1() %></td>
                    <td><%= wifi.getAdres2() %></td>
                    <td><%= wifi.getInstlFloor() %></td>
                    <td><%= wifi.getInstlTy() %></td>
                    <td><%= wifi.getInstlMby() %></td>
                    <td><%= wifi.getSvcSe() %></td>
                    <td><%= wifi.getCmcwr() %></td>
                    <td><%= wifi.getCnstcYear() %></td>
                    <td><%= wifi.getInoutDoor() %></td>
                    <td><%= wifi.getRemars3() %></td>
                    <td><%= wifi.getLat() %></td>
                    <td><%= wifi.getLnt() %></td>
                    <td><%= wifi.getWorkDttm() %></td>
                </tr>
                <%
                            }
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                            out.println("<tr><td colspan='17' style='color: red;'>데이터를 불러오는 중 오류가 발생했습니다.</td></tr>");
                        }
                    } else {
                        out.println("<tr><td colspan='17' style='text-align: center;'>와이파이 데이터를 조회하세요.</td></tr>");
                    }
                %>
                </tbody>
            </table>
        </div>
    </main>

    <!-- JavaScript 파일 불러오기 -->
    <script src="js/functions.js"></script>

</body>
</html>
