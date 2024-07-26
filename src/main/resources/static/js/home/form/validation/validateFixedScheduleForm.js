document.addEventListener('DOMContentLoaded', function() {
    // 지속 시간 변경 시 검증
    document.addEventListener('change', function(event) {
        if (event.target.id === 'schedule-duration-hour' || event.target.id === 'schedule-duration-minute') {
            validateDuration();
        }
    });
});

// 고정 일정에서 타임 슬롯이 하나라도 있으면 공통 입력인 제목, 설명 시작날짜, 종료날짜 필드의 입력을 막음
function toggleFixedFormFields(timeslotCount) {
    const titleInput = document.getElementById('schedule-title');
    const descriptionInput = document.getElementById('schedule-description');
    const startDateInput = document.getElementById('schedule-start-date');
    const endDateInput = document.getElementById('schedule-end-date');
    const saveBtn = document.getElementById('submit-button');
    const disableFields = timeslotCount > 0;

    console.log('disableFields', disableFields);

    titleInput.disabled = disableFields;
    descriptionInput.disabled = disableFields;
    startDateInput.disabled = disableFields;
    endDateInput.disabled = disableFields;
    saveBtn.disabled = (timeslotCount === 0);
}

// 고정일정에서 지속시간이 0시간 0분이면 추가버튼 비활성화
function validateDuration() {

    const title = document.getElementById("schedule-title").value;
    const addTimeslotBtn = document.getElementById('add-timeslot-btn');
    const durationHour = document.getElementById('schedule-duration-hour').value;
    const durationMinute = document.getElementById('schedule-duration-minute').value;

    // 지속 시간이 0시간 0분일 때 추가 버튼 비활성화
    if (parseInt(durationHour, 10) === 0 && parseInt(durationMinute, 10) === 0) {
        addTimeslotBtn.disabled = true;
        showTooltip(addTimeslotBtn, '지속 시간은 최소 1분 이상이어야 합니다.');
    }
    else {
        addTimeslotBtn.disabled = false;
        if (title === "") {
            addTimeslotBtn.disabled = true;
            showTooltip(addTimeslotBtn, '일정의 시간대를 만들기 위해서 제목을 입력해주세요.');
        }
    }
}
