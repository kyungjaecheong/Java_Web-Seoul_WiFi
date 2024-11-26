document.getElementById("location-form")
    .addEventListener("submit", function (event) {
        const lat = document.getElementById("lat").value.trim();
        const lnt = document.getElementById("lnt").value.trim();

        // 숫자 여부 확인 함수
        const isNumeric = (value) => !isNaN(value) && !isNaN(parseFloat(value));

        // 위도와 경도 범위 확인 (위도: -90 ~ 90, 경도: -180 ~ 180)
        const isLatitudeValid = (value) => isNumeric(value) && value >= -90 && value <= 90;
        const isLongitudeValid = (value) => isNumeric(value) && value >= -180 && value <= 180;

        // 빈 값 확인
        if (!lat || !lnt) {
            alert("위도와 경도를 모두 입력해주세요.");
            event.preventDefault(); // 폼 제출 방지
            return false;
        }

        // 숫자 여부 및 범위 확인
        if (!isLatitudeValid(lat)) {
            alert("올바른 위도를 입력해주세요. (범위: -90 ~ 90)");
            event.preventDefault();
            return false;
        }

        if (!isLongitudeValid(lnt)) {
            alert("올바른 경도를 입력해주세요. (범위: -180 ~ 180)");
            event.preventDefault();
            return false;
        }

        // 폼 제출 허용
        return true;
    });


function getMyLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                document.getElementById("lat").value = position.coords.latitude.toFixed(6);
                document.getElementById("lnt").value = position.coords.longitude.toFixed(6);
            },
            function (error) {
                alert("위치 정보를 가져올 수 없습니다:" + error.message);
            }
        );
    } else {
        alert("이 브라우저에서는 Geolocation이 지원되지 않습니다.");
    }
}

// Open API 버튼 동작
function loadOpenAPI() {
    const statusMessage = document.getElementById("status-message");
    const loadingSpinner = document.getElementById("loading-spinner");

    // 초기 상태 설정
    if (statusMessage) {
        statusMessage.innerText = "API 데이터 불러오는 중...";
        statusMessage.className = "loading";
    }
    if (loadingSpinner) {
        loadingSpinner.style.display = "inline-block"; // 스피너 표시
    }

    const request = new XMLHttpRequest();
    request.open("POST", "loadOpenAPI.jsp", true); // JSP 파일로 요청
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    request.onreadystatechange = function () {
        if (request.readyState === XMLHttpRequest.DONE) {
            if (loadingSpinner) {
                loadingSpinner.style.display = "none"; // 스피너 숨기기
            }

            if (request.status === 200) {
                const response = request.responseText.trim();
                if (statusMessage) {
                    statusMessage.innerText = response; // 서버에서 반환된 메시지 출력
                    statusMessage.className = response.includes("오류") ? "error" : "success";
                }
            } else {
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


// 상세 정보 가져오기 (팝업)
function openDetailPopup(mgrNo) {
    const popupUrl = `detailWifi.jsp?mgrNo=${encodeURIComponent(mgrNo)}`;
    const popupOptions = "width=1200,height=800,scrollbars=yes,resizable=yes";
    window.open(popupUrl, "WifiDetailPopup", popupOptions);
}

