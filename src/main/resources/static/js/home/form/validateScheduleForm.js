document.addEventListener('DOMContentLoaded', function() {


    const startDateInput = document.getElementById('schedule-start-date');
    const endDateInput = document.getElementById('schedule-end-date');

    startDateInput.addEventListener('change', handleStartDateChange);
    endDateInput.addEventListener('change', handleEndDateChange);

    // 지속 시간 변경 시 검증
    document.addEventListener('change', function(event) {
        if (event.target.id === 'schedule-duration-hour' || event.target.id === 'schedule-duration-minute') {
            validateDuration();
        }
    });

    // 스타일 추가
    const style = document.createElement('style');
    style.innerHTML = `
        .tooltip {
            position: absolute;
            background-color: #333;
            color: #fff;
            padding: 5px 10px;
            border-radius: 4px;
            width: 20%;
            font-size: 12px;
            opacity: 0;
            transition: opacity 0.3s;
            z-index: 10;
        }
        .tooltip.show {
            opacity: 1;
        }
    `;
    document.head.appendChild(style);
});

// 시작 날짜 검증
function handleStartDateChange() {
    const startDateInput = document.getElementById('schedule-start-date');
    const endDateInput = document.getElementById('schedule-end-date');
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    if (startDate > endDate) {
        endDateInput.value = startDateInput.value;
        showTooltip(endDateInput, '종료 날짜는 시작 날짜와 같거나 더 늦어야 합니다.');
    }
}

// 종료 날짜 검증
function handleEndDateChange() {
    const startDateInput = document.getElementById('schedule-start-date');
    const endDateInput = document.getElementById('schedule-end-date');
    const startDate = new Date(startDateInput.value);
    const endDate = new Date(endDateInput.value);

    if (endDate < startDate) {
        startDateInput.value = endDateInput.value;
        showTooltip(startDateInput, '시작 날짜는 종료 날짜와 같거나 더 빨라야 합니다.');
    }
}

// 변동일정 같은 날짜의 시작시간 검증
function addVariableStartTimeInputListeners() {
    const startTimeInputs = document.querySelectorAll('#start-time-input-group select');
    startTimeInputs.forEach(input => {
        input.addEventListener('change', handleStartTimeChange);
    });
}

function handleStartTimeChange(event) {
    const targetId = event.target.id;
    const startDate = document.getElementById('schedule-start-date').value;
    const endDate = document.getElementById('schedule-end-date').value;

    console.log("HandleTimeChange")
    if (document.getElementById('schedule-form').dataset.type === 'variable' && (startDate === endDate)) {
        validateVariableScheduleTimes(startDate, endDate, 'start');
    }
}

// 변동일정 같은 날짜의 종료시간 검증
function addVariableEndTimeInputListeners() {
    const endTimeInputs = document.querySelectorAll('#end-time-input-group select');
    endTimeInputs.forEach(input => {
        input.addEventListener('change', handleEndTimeChange);
    });
}

function handleEndTimeChange(event) {
    const targetId = event.target.id;
    const startDate = document.getElementById('schedule-start-date').value;
    const endDate = document.getElementById('schedule-end-date').value;

    console.log("HandleTimeChange")
    if (document.getElementById('schedule-form').dataset.type === 'variable' && (startDate === endDate)) {
        validateVariableScheduleTimes(startDate, endDate, 'end');
    }
}

// 변동 일정
function validateVariableScheduleTimes(startDate, endDate, type) {
    const startTimeInput = document.getElementById('schedule-start-time');
    const endTimeInput = document.getElementById('schedule-end-time');
    const startHourInput = document.getElementById('schedule-start-hour');
    const startMinuteInput = document.getElementById('schedule-start-minute');
    const endHourInput = document.getElementById('schedule-end-hour');
    const endMinuteInput = document.getElementById('schedule-end-minute');

    const startHour = parseInt(startHourInput.value, 10);
    const startMinute = parseInt(startMinuteInput.value, 10);
    const endHour = parseInt(endHourInput.value, 10);
    const endMinute = parseInt(endMinuteInput.value, 10);

    let startFullHour = startTimeInput.value === 'PM' && startHour !== 12 ? startHour + 12 : startHour;
    startFullHour = startTimeInput.value === 'AM' && startHour === 12 ? 0 : startFullHour;

    let endFullHour = endTimeInput.value === 'PM' && endHour !== 12 ? endHour + 12 : endHour;
    endFullHour = endTimeInput.value === 'AM' && endHour === 12 ? 0 : endFullHour;

    const startTime = new Date(`${startDate}T${String(startFullHour).padStart(2, '0')}:${String(startMinute).padStart(2, '0')}:00`);
    const endTime = new Date(`${endDate}T${String(endFullHour).padStart(2, '0')}:${String(endMinute).padStart(2, '0')}:00`);

    if (startTime >= endTime){

        if(type === 'start') {
            showTooltip(startTimeInput, '시작 시간이 종료 시간보다 빨라야 합니다.');
            startTimeInput.value = 'AM';
            startHourInput.value = '12';
            startMinuteInput.value = '00';
        } else if (type === 'end'){
            showTooltip(endTimeInput, '종료 시간이 시작 시간보다 늦어야 합니다.');
            endTimeInput.value = 'PM';
            endHourInput.value = '11';
            endMinuteInput.value = '59';
        }
    }
}

// 고정 일정에서 타임 슬롯이 하나라도 있으면 공통 입력인 제목, 설명 시작날짜, 종료날짜 필드의 입력을 막음
function toggleFormFields(timeslotCount) {
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
    const addTimeslotBtn = document.getElementById('add-timeslot-btn');
    const durationHour = document.getElementById('schedule-duration-hour').value;
    const durationMinute = document.getElementById('schedule-duration-minute').value;

    // 지속 시간이 0시간 0분일 때 추가 버튼 비활성화
    if (parseInt(durationHour, 10) === 0 && parseInt(durationMinute, 10) === 0) {
        addTimeslotBtn.disabled = true;
        showTooltip(addTimeslotBtn, '지속 시간은 최소 1분 이상이어야 합니다.');
    } else {
        addTimeslotBtn.disabled = false;
    }
}


function showTooltip(element, message) {
    const tooltip = document.createElement('div');
    tooltip.className = 'tooltip';
    tooltip.textContent = message;
    document.body.appendChild(tooltip);

    const rect = element.getBoundingClientRect();
    tooltip.style.left = `${rect.left + window.scrollX + element.offsetWidth / 2 - tooltip.offsetWidth / 2}px`;
    tooltip.style.top = `${rect.top + window.scrollY - tooltip.offsetHeight - 10}px`;

    // 툴팁을 즉시 표시하고 잠시 후 사라지도록 설정
    requestAnimationFrame(() => {
        tooltip.classList.add('show');
        setTimeout(() => {
            tooltip.remove();
        }, 3000);
    });
}



