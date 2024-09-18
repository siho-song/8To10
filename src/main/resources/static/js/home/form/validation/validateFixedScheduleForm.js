document.addEventListener('DOMContentLoaded', function() {
    // 지속 시간 변경 시 검증
    document.addEventListener('change', function(event) {

        // 지속 시간 및 제목 입력 변경 시 검증
        document.addEventListener('change', function(event) {
            if (['schedule-duration-hour', 'schedule-duration-minute', 'schedule-title', 'weekday-mo', 'weekday-tu', 'weekday-we', 'weekday-th', 'weekday-fr', 'weekday-sa', 'weekday-su'].includes(event.target.id)) {
                console.log("change 이벤트 리스너 실행 (validateFixedScheduleForm.js)")
                // validateTimeslotCreation();
            }
        });

    });
});

function isValidTitle() {
    const titleInput = document.getElementById('schedule-title');
    return titleInput.value.trim() !== '';
}

function isValidDurationTime() {
    const durationHour = document.getElementById('schedule-duration-hour').value;
    const durationMinute = document.getElementById('schedule-duration-minute').value;

    return !(parseInt(durationHour, 10) === 0 && parseInt(durationMinute, 10) === 0);
}

function isValidWeekDays(weekdays) {
    return weekdays.length > 0;
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