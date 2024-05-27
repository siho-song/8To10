// 일정 추가를 눌렀을 떄 나오는 (고정,일반,변동) 일정 유형 팝업 표시 관련 기능

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('toggle-add-schedule-btn').addEventListener('click', function() {
        togglePanel('schedule-type-popup');
    });
});

function createScheduleForm(type) {
    document.getElementById('schedule-type-popup').style.display = 'none';
    document.getElementById('schedule-form-container').style.display = 'block';

    const additionalFields = document.getElementById('additional-fields');
    additionalFields.innerHTML = '';

    if (type === 'fixed') {  <!-- 고정 일정-->
        document.getElementById('add-timeslot-btn').style.display = 'block';

        additionalFields.innerHTML = `
            <div class="form-group">
                <label for="schedule-start-hour">시작 시간</label>
                 <div class="time-input-group">
                    <select id="schedule-start-time" name="startTime">
                        <option value="AM">오전</option>
                        <option value="PM">오후</option>
                    </select>
                    <select id="schedule-start-hour" name="startHour">
                        <!-- 시간 옵션 -->
                    </select>
                    <select id="schedule-start-minute" name="startMinute">
                        <!-- 분 옵션 -->
                    </select>
                 </div>
            </div>
            <div class="form-group">
                <label for="schedule-duration-hour">지속 시간</label>
                <div class="time-input-group">
                    <select id="schedule-duration-hour" name="durationHour">
                        <!-- 시간 옵션 -->
                    </select>
                    <select id="schedule-duration-minute" name="durationMinute">
                        <!-- 분 옵션 -->
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="schedule-frequency">빈도</label>
                <select id="schedule-frequency" name="frequency" onchange="handleFrequencyChange()">
                    <option value="daily">매일</option>
                    <option value="weekly">매주</option>
                    <option value="biweekly">격주</option>
                </select>
            </div>
            <div class="form-group">
                <label>수행 요일</label>
                <div class="weekday-checkbox-group">
                    <label for="weekday-mo"><input type="checkbox" id="weekday-mo" name="days" value="MO">월</label>
                    <label for="weekday-tu"><input type="checkbox" id="weekday-tu" name="days" value="TU">화</label>
                    <label for="weekday-we"><input type="checkbox" id="weekday-we" name="days" value="WE">수</label>
                    <label for="weekday-th"><input type="checkbox" id="weekday-th" name="days" value="TH">목</label>
                    <label for="weekday-fr"><input type="checkbox" id="weekday-fr" name="days" value="FR">금</label>
                    <label for="weekday-sa"><input type="checkbox" id="weekday-sa" name="days" value="SA">토</label>
                    <label for="weekday-su"><input type="checkbox" id="weekday-su" name="days" value="SU">일</label>
                </div>
            </div>
            <div class="form-group">
                <div class="timeslot-container">
                    <!-- 타임 슬롯을 임시로 저장해두기 위한 컨테이너-->
                </div>
            </div>
        `;
        initializeFixedTimeOptions();
        handleFrequencyChange(); // 초기 빈도 값 설정
    }
    else if (type === 'normal') {   <!-- 일반 일정-->
        document.getElementById('add-timeslot-btn').style.display = 'none';
        additionalFields.innerHTML = `
            <div class="form-group">
                    <label for="schedule-buffer-time">희망 여유 시간</label>
                    <select id="schedule-buffer-hour" name="bufferHour">
                    <!-- 시간 옵션 -->
                    </select>
                    <select id="schedule-buffer-minute" name="bufferMinute">
                        <!-- 분 옵션 -->
                    </select>
            </div>
            <div class="form-group">
                <label for="schedule-days">수행 요일</label>
                <div class="weekday-checkbox-group">
                        <label for="weekday-mo"><input type="checkbox" id="weekday-mo" name="days" value="mo" checked> 월</label>
                        <label for="weekday-tu"><input type="checkbox" id="weekday-tu" name="days" value="tu" checked> 화</label>
                        <label for="weekday-we"><input type="checkbox" id="weekday-we" name="days" value="we" checked> 수</label>
                        <label for="weekday-th"><input type="checkbox" id="weekday-th" name="days" value="th" checked> 목</label>
                        <label for="weekday-fr"><input type="checkbox" id="weekday-fr" name="days" value="fr" checked> 금</label>
                        <label for="weekday-sa"><input type="checkbox" id="weekday-sa" name="days" value="sa" checked> 토</label>
                        <label for="weekday-su"><input type="checkbox" id="weekday-su" name="days" value="su" checked> 일</label>
                </div>
            </div>
        `;
        initializeBufferTimeOptions();
    }
    else if (type === 'variable') {   <!-- 변동 일정-->
        document.getElementById('add-timeslot-btn').style.display = 'none';
        additionalFields.innerHTML = `
            <div class="form-group">
                    <label for="schedule-start-time">시작 시간</label>
                    <div class="time-input-group">
                        <select id="schedule-start-time" name="startTime">
                            <option value="AM">오전</option>
                            <option value="PM">오후</option>
                        </select>
                        <select id="schedule-start-hour" name="startHour">
                            <!-- 시간 옵션 -->
                        </select>
                        <select id="schedule-start-minute" name="startMinute">
                            <!-- 분 옵션 -->
                        </select>
                    </div>
            </div>
            <div class="form-group">
                <label for="schedule-end-time">종료 시간</label>
                <div class="time-input-group">
                        <select id="schedule-end-time" name="endTime">
                            <option value="AM">오전</option>
                            <option value="PM">오후</option>
                        </select>
                        <select id="schedule-end-hour" name="endHour">
                            <!-- 시간 옵션 -->
                        </select>
                        <select id="schedule-end-minute" name="endMinute">
                            <!-- 분 옵션 -->
                        </select>
                    </div>
            </div>
        `;
        initializeTimeOptions();
    }
    document.getElementById('schedule-form').dataset.type = type;
    setDefaultDate();
}

// 요일 선택 시에 '매일' 옵션이 들어가면 체크박스 비활성화
function handleFrequencyChange() {
    const frequency = document.getElementById('schedule-frequency').value;
    const weekdayCheckboxes = document.querySelectorAll('input[name="days"]');

    console.log(frequency)

    if (frequency === 'daily') {
        weekdayCheckboxes.forEach(checkbox => {
            checkbox.checked = true;
            checkbox.disabled = true;
        });
    } else {
        weekdayCheckboxes.forEach(checkbox => {
            checkbox.checked = false;
            checkbox.disabled = false;
        });
    }
}


function createTimeSlot(timeslots) {
    const startPeriod = document.getElementById('schedule-start-time').value;
    const startHour = document.getElementById('schedule-start-hour').value;
    const startMinute = document.getElementById('schedule-start-minute').value;
    const durationHour = document.getElementById('schedule-duration-hour').value;
    const durationMinute = document.getElementById('schedule-duration-minute').value;
    const frequency = document.getElementById('schedule-frequency').value;
    const weekdays = Array.from(document.querySelectorAll('input[name="days"]:checked')).map(checkbox => checkbox.value);

    // 시작 시간 계산
    let startTime = `${startHour}:${startMinute}:00`;

    if (startPeriod === 'AM') {
        if(startHour === '12') {
            startTime = `00:${startMinute}:00`;
        }
    } else {
        if (startHour !== '12') {
            startTime = `${parseInt(startTime) + 12}:${startMinute}:00`;
        }
    }

    // 지속 시간 계산
    let endPeriod = startPeriod;
    let endTimeHour = parseInt(startHour) + parseInt(durationHour);
    let endTimeMinute = parseInt(startMinute) + parseInt(durationMinute);
    if (endTimeMinute >= 60) {
        endTimeHour += Math.floor(endTimeMinute / 60);
        endTimeMinute = endTimeMinute % 60;
    }

    if (endTimeHour >= 12) {
        endPeriod = endTimeHour >= 24 ? 'AM' : 'PM';
        if (endTimeHour > 24) {
            endTimeHour = endTimeHour - 24;
        } else if (endTimeHour >= 12) {
            endTimeHour = endTimeHour - 12;
        }
    }

    if (endTimeHour === 0) {
        endTimeHour = 12;
    }

    let endTimeWithPeriod = `${endTimeHour < 10 ? '0' : ''}${endTimeHour}:${endTimeMinute < 10 ? '0' : ''}${endTimeMinute}`;
    let endTime = calculateEndTime(startPeriod, startHour, startMinute, durationHour, durationMinute);

    const timeslot = {
        weekdays: weekdays,
        startTimeWithPeriod: `${startPeriod} ${startHour}:${startMinute}`,
        startTime: startTime,
        endTimeWithPeriod: `${endPeriod} ${endTimeWithPeriod}`,
        endTime: endTime,
        frequency: frequency,
        days: weekdays,
        duration:`${durationHour}:${durationMinute}`
    };

    timeslots.push(timeslot);
    renderTimeslots(timeslots);
}

function renderTimeslots(timeslots) {
    const container = document.querySelector('.timeslot-container');
    container.innerHTML = '';

    timeslots.forEach((slot, index) => {
        const item = document.createElement('div');
        item.className = 'timeslot-item';
        const buttonId = `delete-btn-${index}`;
        item.innerHTML = `
            <div>
                <strong>${slot.weekdays.join(', ')}</strong> (${slot.frequency}) : ${slot.startTime} ~ ${slot.endTime}
            </div>
            <button type="button" id="${buttonId}">삭제</button>
        `;

        container.appendChild(item);

        const deleteButton = document.getElementById(buttonId);
        deleteButton.addEventListener('click', () => {
            removeTimeslot(timeslots, index);
        });
    });


}

function removeTimeslot(timeslots, index) {
    timeslots.splice(index, 1);
    console.log("삭제", timeslots)

    renderTimeslots(timeslots);
}
