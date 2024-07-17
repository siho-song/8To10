// 일정 추가를 눌렀을 떄 나오는 (고정,일반,변동) 일정 유형 팝업 표시 관련 기능

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('toggle-add-schedule-btn').addEventListener('click', function() {
        togglePanel('schedule-type-popup');
    });
});

function createScheduleForm(type) {
    document.getElementById('schedule-form-header').textContent = "";

    document.getElementById('schedule-type-popup').style.display = 'none';
    document.getElementById('schedule-form-container').style.display = 'block';

    const additionalFields = document.getElementById('additional-fields');
    additionalFields.innerHTML = '';

    initializeInputField();

    if (type === 'fixed') {  <!-- 고정 일정-->
        document.getElementById('schedule-form-header').textContent = "고정 일정";
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
                <div class="time-input-group" id="duration-time-input-group">
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
                    <label for="weekday-mo"><input type="checkbox" id="weekday-mo" name="days" value="mo">월</label>
                    <label for="weekday-tu"><input type="checkbox" id="weekday-tu" name="days" value="tu">화</label>
                    <label for="weekday-we"><input type="checkbox" id="weekday-we" name="days" value="we">수</label>
                    <label for="weekday-th"><input type="checkbox" id="weekday-th" name="days" value="th">목</label>
                    <label for="weekday-fr"><input type="checkbox" id="weekday-fr" name="days" value="fr">금</label>
                    <label for="weekday-sa"><input type="checkbox" id="weekday-sa" name="days" value="sa">토</label>
                    <label for="weekday-su"><input type="checkbox" id="weekday-su" name="days" value="su">일</label>
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
        document.getElementById('schedule-form-header').textContent = "일반 일정";
        document.getElementById('add-timeslot-btn').style.display = 'none';
        additionalFields.innerHTML = `
            <div class="form-group">
                <label for="schedule-buffer-time">희망 여유 시간</label>
                <div class="time-input-group" id="buffer-time-input-group">
                    <select id="schedule-buffer-hour" name="bufferHour"></select>
                    <select id="schedule-buffer-minute" name="bufferMinute"></select>
                </div>
            </div>
            <div class="form-group">
                <label for="schedule-total-amount">목표 달성 총량</label>
                <input type="text" id="schedule-total-amount" name="totalAmount" placeholder="달성하고 싶은 양">
            </div>
            <div class="form-group">
                <label for="schedule-perform-in-day">희망 하루 수행시간</label>
                <div class="time-input-group" id="perform-in-day-input-group">
                    <select id="schedule-perform-hour" name="performHour"></select>
                    <select id="schedule-perform-minute" name="performMinute"></select>
                </div>
            </div>
            <div class="form-group">
                <label for="schedule-perform-in-week">희망 주간 수행빈도</label>
                <select id="schedule-perform-in-week" name="performInWeek">
                    <option value="1">1회</option>
                    <option value="2">2회</option>
                    <option value="3">3회</option>
                    <option value="4">4회</option>
                    <option value="5">5회</option>
                    <option value="6">6회</option>
                    <option value="7">7회</option>
                </select>
            </div>
            <div class="form-group">
            <label for="include-weekend">주말 포함 여부</label>
            <div>
                <label><input type="checkbox" id="include-saturday" name="includeSaturday"> 토요일 포함</label>
                <label><input type="checkbox" id="include-sunday" name="includeSunday"> 일요일 포함</label>
            </div>
        </div>
        `;
        initializeNormalTimeOptions();
    }

    else if (type === 'variable') {   <!-- 변동 일정-->
        document.getElementById('schedule-form-header').textContent = "변동 일정";
        document.getElementById('add-timeslot-btn').style.display = 'none';
        additionalFields.innerHTML = `
            <div class="form-group">
                    <label for="schedule-start-time">시작 시간</label>
                    <div class="time-input-group" id="start-time-input-group">
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
                <div class="time-input-group" id="end-time-input-group">
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
        addVariableStartTimeInputListeners();
        addVariableEndTimeInputListeners();
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

    const timeslot = {
        startTime: startTime,
        duration:`${durationHour}:${durationMinute}`,
        frequency: frequency,
        days: weekdays,
    };

    timeslots.push(timeslot);

    resetFixedScheduleForm();
    renderTimeslots(timeslots);
    toggleFormFields(timeslots.length);
    console.log("timeslots.length", timeslots.length);
}

function renderTimeslots(timeslots) {
    const container = document.querySelector('.timeslot-container');
    container.innerHTML = '';

    timeslots.forEach((slot, index) => {
        const item = document.createElement('div');
        item.className = 'timeslot-item';
        const buttonId = `delete-btn-${index}`;
        item.innerHTML = `
            <div class="slot-box">
                <div class="slot-info">
                    <p>빈도 : ${slot.frequency}</p>
                    <p>요일 : ${slot.days.join(', ')}</p> 
                    <p>시작 시간 :${slot.startTime.substring(0, 6)}</p>
                    <p>지속 시간 : ${slot.duration}</p>
                </div>
                <button type="button" class="slot-delete-btn" id="${buttonId}">삭제</button>
            </div>
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
    toggleFormFields(timeslots.length);
    validateDuration();
}

function initializeInputField() {
    // 처음 렌더링 될 때 내용 초기화, 저장 버튼 비활성화
    document.getElementById('schedule-title').value ="";
    document.getElementById('schedule-description').value ="";
    document.getElementById('submit-button').disabled = true;
}

function resetFixedScheduleForm() {
    document.getElementById('schedule-start-time').value = "AM";
    document.getElementById('schedule-start-hour').value = "01";
    document.getElementById('schedule-start-minute').value = "00";
    document.getElementById('schedule-duration-hour').value = "00";
    document.getElementById('schedule-duration-minute').value = "00";
    document.getElementById('schedule-frequency').value = "daily";
    document.querySelectorAll('input[name="days"]').forEach(checkbox => {
        checkbox.checked = true;
        checkbox.disabled = true;
    });
}