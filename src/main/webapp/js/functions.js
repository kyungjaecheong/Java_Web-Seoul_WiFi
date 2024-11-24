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

function loadOpenAPI() {
    alert("Open API 정보를 가져오는 기능이 호출되었습니다. (현재 서버 문제로 테스트는 불가능)");
}

// 테이블 초기화 (옵션)
function resetTable() {
    const tableBody = document.querySelector("table tbody");
    tableBody.innerHTML = `
        <tr>
            <td colspan="10" style="text-align: center;">와이파이 데이터를 조회하세요.</td>
        </tr>`;
}