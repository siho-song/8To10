// // 일정을 클릭했을 때 나오는 일정 상세보기 폼 관련 기능
function displayEventDetailsInSidebar(event) {

    // 기본 일정 상세보기 HTML 생성
    let detailsHTML = `
        <strong>일정 제목:</strong> ${event.title}<br>
        <strong>시작 시간:</strong> ${event.start.toLocaleString()}<br>
    `;

    if (event.end) {
        detailsHTML += `<strong>종료 시간:</strong> ${event.end.toLocaleString()}<br>`;
    }
    if (event.extendedProps.commonDescription) {
        detailsHTML += `<strong>일정 메모:</strong> ${event.extendedProps.commonDescription}<br>`;
    }
    detailsHTML += `<button onclick="togglePanel('')">닫기</button>`;
    // 상세보기 컨테이너에 HTML 설정
    const detailsContainer = document.getElementById('event-details-container');
    detailsContainer.innerHTML = detailsHTML;
    togglePanel('event-details-container');
}
