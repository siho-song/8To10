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
        additionalFields.innerHTML = `
            <div class="form-group">
                    <label for="schedule-start-time">시작 시간</label>
                    <select id="schedule-start-time" name="startTime">
                        <!-- 오전 시간 -->
                        <option value="AM">오전</option>
                        <!-- 오후 시간 -->
                        <option value="PM">오후</option>
                    </select>
                    <select id="schedule-start-hour" name="startHour">
                        <!-- 시간 옵션 -->
                    </select>
                    <select id="schedule-start-minute" name="startMinute">
                        <!-- 분 옵션 -->
                    </select>
            </div>
            <div class="form-group">
                <label for="schedule-end-time">종료 시간</label>
                <select id="schedule-end-time" name="endTime">
                    <!-- 오전 시간 -->
                    <option value="AM">오전</option>
                    <!-- 오후 시간 -->
                    <option value="PM">오후</option>
                </select>
                <select id="schedule-end-hour" name="endHour">
                    <!-- 시간 옵션 -->
                </select>
                <select id="schedule-end-minute" name="endMinute">
                    <!-- 분 옵션 -->
                </select>
            </div>
            <div class="form-group">
                <label for="schedule-frequency">빈도</label>
                <select id="schedule-frequency" name="frequency">
                    <option value="daily">매일</option>
                    <option value="weekly">매주</option>
                    <option value="monthly">매월</option>
                </select>
            </div>
            <div class="form-group">
                <label>수행 요일</label>
                <label><input type="checkbox" name="weekdays" value="MO">월요일</label>
                <label><input type="checkbox" name="weekdays" value="TU">화요일</label>
                <label><input type="checkbox" name="weekdays" value="WE">수요일</label>
                <label><input type="checkbox" name="weekdays" value="TH">목요일</label>
                <label><input type="checkbox" name="weekdays" value="FR">금요일</label>
                <label><input type="checkbox" name="weekdays" value="SA">토요일</label>
                <label><input type="checkbox" name="weekdays" value="SU">일요일</label>
            </div>
<!--            TODO 추가버튼 구현해야함-->
        `;
        initializeTimeOptions();

    }
    else if (type === 'normal') {   <!-- 일반 일정-->
        additionalFields.innerHTML = `
            <div class="form-group">
                    <label for="schedule-end-time">희망 여유 시간</label>
                    <select id="schedule-buffer-hour" name="bufferHour">
                    <!-- 시간 옵션 -->
                    </select>
                    <select id="schedule-buffer-minute" name="bufferMinute">
                        <!-- 분 옵션 -->
                    </select>
            </div>
            <div class="form-group">
                <label for="schedule-days">수행 요일</label>
                <div>
                    <label><input type="checkbox" name="days" value="mo" checked> 월요일</label>
                    <label><input type="checkbox" name="days" value="tu" checked> 화요일</label>
                    <label><input type="checkbox" name="days" value="we" checked> 수요일</label>
                    <label><input type="checkbox" name="days" value="th" checked> 목요일</label>
                    <label><input type="checkbox" name="days" value="fr" checked> 금요일</label>
                    <label><input type="checkbox" name="days" value="sa" checked> 토요일</label>
                    <label><input type="checkbox" name="days" value="su" checked> 일요일</label>
                </div>
            </div>
        `;
        initializeBufferTimeOptions();
    }
    else if (type === 'variable') {   <!-- 변동 일정-->
        additionalFields.innerHTML = `
            <div class="form-group">
                    <label for="schedule-start-time">시작 시간</label>
                    <select id="schedule-start-time" name="startTime">
                        <!-- 오전 시간 -->
                        <option value="AM">오전</option>
                        <!-- 오후 시간 -->
                        <option value="PM">오후</option>
                    </select>
                    <select id="schedule-start-hour" name="startHour">
                        <!-- 시간 옵션 -->
                    </select>
                    <select id="schedule-start-minute" name="startMinute">
                        <!-- 분 옵션 -->
                    </select>
            </div>
            <div class="form-group">
                <label for="schedule-end-time">종료 시간</label>
                <select id="schedule-end-time" name="endTime">
                    <!-- 오전 시간 -->
                    <option value="AM">오전</option>
                    <!-- 오후 시간 -->
                    <option value="PM">오후</option>
                </select>
                <select id="schedule-end-hour" name="endHour">
                    <!-- 시간 옵션 -->
                </select>
                <select id="schedule-end-minute" name="endMinute">
                    <!-- 분 옵션 -->
                </select>
            </div>
        `;
        initializeTimeOptions();
    }
    document.getElementById('schedule-form').dataset.type = type;
    setDefaultDate();
}

