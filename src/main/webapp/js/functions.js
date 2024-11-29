// location-form의 submit 이벤트 리스너 추가
document.getElementById("location-form")
    .addEventListener("submit", function (event) {
        // 입력된 위도 값
        const lat = document.getElementById("lat").value.trim();
        // 입력된 경도 값
        const lnt = document.getElementById("lnt").value.trim();

        // 숫자인지 확인하는 함수 (NaN 여부 검사)
        const isNumeric = (value) => !isNaN(value) && !isNaN(parseFloat(value));

        // 위도와 경도 범위 확인 (위도: -90 ~ 90, 경도: -180 ~ 180)
        const isLatitudeValid = (value) => isNumeric(value) && value >= -90 && value <= 90;
        const isLongitudeValid = (value) => isNumeric(value) && value >= -180 && value <= 180;

        // 위도와 경도가 비어 있는지 확인
        if (!lat || !lnt) {
            alert("위도와 경도를 모두 입력해주세요.");
            event.preventDefault(); // 폼 제출 방지
            return false;
        }

        // 위도가 유효한지 확인
        if (!isLatitudeValid(lat)) {
            alert("올바른 위도를 입력해주세요. (범위: -90 ~ 90)");
            event.preventDefault(); // 폼 제출 방지
            return false;
        }

        // 경도가 유효한지 확인
        if (!isLongitudeValid(lnt)) {
            alert("올바른 경도를 입력해주세요. (범위: -180 ~ 180)");
            event.preventDefault(); // 폼 제출 방지
            return false;
        }

        // 모든 검증을 통과한 경우 폼 제출 허용
        return true;
    });

// 현재 위치 정보 가져오기 (Geolocation API 사용)
function getMyLocation() {
    // Geolocation API 지원 여부 확인
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                // 현재 위치 정보를 입력 필드에 설정 (소수점 6자리)
                document.getElementById("lat").value = position.coords.latitude.toFixed(6);
                document.getElementById("lnt").value = position.coords.longitude.toFixed(6);
            },
            function (error) {
                // 위치 정보를 가져오는 도중 에러 발생 시 알림 표시
                alert("위치 정보를 가져올 수 없습니다:" + error.message);
            }
        );
    } else {
        // Geolocation API를 지원하지 않는 브라우저일 경우
        alert("이 브라우저에서는 Geolocation이 지원되지 않습니다.");
    }
}

// Open API 버튼 클릭 시 호출되는 함수
function loadOpenAPI() {
    // 상태 메시지 표시 요소
    const statusMessage = document.getElementById("status-message");
    // 로딩 스피너 요소
    const loadingSpinner = document.getElementById("loading-spinner");

    // 상태 초기화
    if (statusMessage) {
        statusMessage.innerText = "API 데이터 불러오는 중...";  // 메시지 업데이트
        statusMessage.className = "loading";    // 상태 스타일 설정
    }
    if (loadingSpinner) {
        loadingSpinner.style.display = "inline-block"; // 스피너 표시
    }

    // AJAX 요청 객체 생성
    const request = new XMLHttpRequest();
    // loadOpenAPI.jsp에 POST 요청
    request.open("POST", "loadOpenAPI.jsp", true);
    // 요청 헤더 설정
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    // 요청 상태 변경 이벤트 처리
    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (loadingSpinner) {
                loadingSpinner.style.display = "none"; // 스피너 숨기기
            }

            // 요청 성공
            if (request.status === 200) {
                // 서버 응답 메시지
                const response = request.responseText.trim();
                if (statusMessage) {
                    // 상태 메시지 업데이트
                    statusMessage.innerText = response; // 서버에서 반환된 메시지 출력
                    statusMessage.className = response.includes("오류") ? "error" : "success";
                }
            } else {
            // 요청 실패
                if (statusMessage) {
                    statusMessage.innerText = "서버 요청 실패. 다시 시도하세요.";
                    statusMessage.className = "error";
                }
            }
        }
    };

    // 서버로 요청 전송
    request.send();
}


// 와이파이 상세 정보를 팝업으로 표시하는 함수
function openDetailPopup(mgrNo) {
    // 팝업 URL에 mgrNo를 쿼리 매개변수로 추가
    const popupUrl = `../detailWifi.jsp?mgrNo=${encodeURIComponent(mgrNo)}`;

    // 팝업 옵션 설정
    const popupOptions = "width=1200,height=800,scrollbars=yes,resizable=yes";

    // 팝업 창 열기
    window.open(popupUrl, "WifiDetailPopup", popupOptions);
}

