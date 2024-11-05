// // 패널 토글 함수 개선

function togglePanel(panelId) {
    const panels = ['add-schedule-form-container', 'event-details-container', 'schedule-type-popup'];

    panels.forEach(id => {
        const panel = document.getElementById(id);
        if (panel) {
            if (id === panelId) {
                panel.style.display = panel.style.display === 'block' ? 'none' : 'block';
            } else {
                panel.style.display = 'none'; // 다른 패널은 숨깁니다.
            }
        }
    });
}
