document.addEventListener('DOMContentLoaded', function() {
    // 지속 시간 변경 시 검증
    document.addEventListener('change', function(event) {

        // 지속 시간 및 제목 입력 변경 시 검증
        document.addEventListener('change', function(event) {
            if (['schedule-duration-hour', 'schedule-duration-minute', 'schedule-title'].includes(event.target.id)) {
                validateTimeslotCreation();
            }
        });

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

// 지속시간이 0시간 0분이거나 title이 비어있다면 타임슬롯 추가 버튼을 사용할 수 없다.
function validateTimeslotCreation() {
    const titleInput = document.getElementById('schedule-title');
    const durationHour = document.getElementById('schedule-duration-hour').value;
    const durationMinute = document.getElementById('schedule-duration-minute').value;
    const addTimeslotBtn = document.getElementById('add-timeslot-btn');

    const isTitleValid = titleInput.value.trim() !== '';
    const isDurationValid = !(parseInt(durationHour, 10) === 0 && parseInt(durationMinute, 10) === 0);

    if (isTitleValid && isDurationValid) {
        addTimeslotBtn.disabled = false;
    } else {
        addTimeslotBtn.disabled = true;
        let message = '';
        if (!isTitleValid) {
            message = '제목을 입력해주세요.';
        } else if (!isDurationValid) {
            message = '지속 시간은 최소 1분 이상이어야 합니다.';
        }
        showTooltip(addTimeslotBtn, message);
    }
}


// 겹치는 시간슬롯이 있는지를 검증하기 위한 메소드
function validateTimeSlotOverlap(timeslots, newTimeslot) {
    for (let existingTimeslot of timeslots) {
        // 요일이 겹치는지 확인
        const overlappingDays = existingTimeslot.days.filter(day => newTimeslot.days.includes(day));

        if (overlappingDays.length > 0) {
            const existingStartTime = convertToMinutes(existingTimeslot.startTime);
            const existingEndTime = existingStartTime + convertDurationToMinutes(existingTimeslot.duration);
            const newStartTime = convertToMinutes(newTimeslot.startTime);
            const newEndTime = newStartTime + convertDurationToMinutes(newTimeslot.duration);

            // 시간 겹침 여부 검사
            if (Math.max(existingStartTime, newStartTime) < Math.min(existingEndTime, newEndTime)) {
                return false;
            }
        }
    }
    return true;
}

function convertToMinutes(timeStr) {
    const [hours, minutes] = timeStr.split(':').map(Number);
    return hours * 60 + minutes;
}

function convertDurationToMinutes(durationStr) {
    const [hours, minutes] = durationStr.split(':').map(Number);
    return hours * 60 + minutes;
}

function isTitleEmpty() {
    const titleInputArea = document.getElementById("schedule-title").value;
    console.log(titleInputArea);

    return titleInputArea.length === 0;
}