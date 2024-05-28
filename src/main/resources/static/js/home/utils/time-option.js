function initializeTimeOptions() {
    fillTimeSelect('schedule-start-hour', 'schedule-start-minute');
    fillTimeSelect('schedule-end-hour', 'schedule-end-minute');
}

// 고정 일정은 종료시간이 아닌 duration을 받기 때문에 따로 처리 해주는 함수
// 시간, 분 단위로 초기화
function initializeFixedTimeOptions() {
    fillTimeSelect('schedule-start-hour', 'schedule-start-minute');
    fillBufferTimeSelect('schedule-duration-hour', 'schedule-duration-minute');
}


//일반 일정은 시간을 받지 않기 때문에 따로 처리해주는 함수
//시간,분 단위로 선택창을 초기화한다.
function initializeBufferTimeOptions() {
    fillBufferTimeSelect('schedule-buffer-hour', 'schedule-buffer-minute'); // 추가된 희망 여유 시간 필드 초기화
}

// 취침시간, 기상시간을 설정
function initializeSleepWakeTimeOptions() {
    fillTimeSelect('sleep-time-hour', 'sleep-time-minute');
    fillTimeSelect('wake-time-hour', 'wake-time-minute');
}

function fillTimeSelect(hourId, minuteId) {
    const hourSelect = document.getElementById(hourId);
    const minuteSelect = document.getElementById(minuteId);

    for (let i = 1; i <= 12; i++) {
        let hourOption = new Option(`${i}시`, i < 10 ? '0' + i : i.toString());
        hourSelect.add(hourOption);
    }

    for (let i = 0; i < 60; i++) {
        let minuteOption = new Option(`${i}분`, i < 10 ? '0' + i : i.toString());
        minuteSelect.add(minuteOption);
    }
}

function fillBufferTimeSelect(hourId, minuteId) {
    const hourSelect = document.getElementById(hourId);
    const minuteSelect = document.getElementById(minuteId);

    for (let i = 0; i <= 23; i++) {
        let hourOption = new Option(`${i}시간`, i < 10 ? '0' + i : i.toString());
        hourSelect.add(hourOption);
    }

    for (let i = 0; i < 60; i++) {
        let minuteOption = new Option(`${i}분`, i < 10 ? '0' + i : i.toString());
        minuteSelect.add(minuteOption);
    }
}

function convertTo24HourFormat(date, hour, minute) {
    hour = parseInt(hour) % 12 + (hour >= 12 ? 12 : 0); // Convert hour to 24-hour format
    return `${date}T${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00`;
}

