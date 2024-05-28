function validateSleepTime() {
    const sleepPeriod = document.getElementById('sleep-time-period').value;
    const sleepHour = document.getElementById('sleep-time-hour').value;
    const sleepMinute = document.getElementById('sleep-time-minute').value;

    const sleepTime = convertTo24HourFormat('1970-01-01', sleepPeriod === 'PM' ? parseInt(sleepHour) + 12 : sleepHour, sleepMinute);
    const wakeTime = convertTo24HourFormat('1970-01-01', wakePeriod === 'PM' ? parseInt(wakeHour) + 12 : wakeHour, wakeMinute);

    if (sleepTime >= wakeTime) {
        showTooltip(document.getElementById('sleep-time-hour'), '취침 시간은 기상 시간보다 빨라야 합니다.');
        // 적절한 기본 값으로 초기화
        document.getElementById('sleep-time-period').value = "PM";
        document.getElementById('sleep-time-hour').value = "10";
        document.getElementById('sleep-time-minute').value = "00";
    }
}

function validateWakeUpTime() {
    const wakePeriod = document.getElementById('wake-time-period').value;
    const wakeHour = document.getElementById('wake-time-hour').value;
    const wakeMinute = document.getElementById('wake-time-minute').value;

    const sleepTime = convertTo24HourFormat('1970-01-01', sleepPeriod === 'PM' ? parseInt(sleepHour) + 12 : sleepHour, sleepMinute);
    const wakeTime = convertTo24HourFormat('1970-01-01', wakePeriod === 'PM' ? parseInt(wakeHour) + 12 : wakeHour, wakeMinute);

    if (sleepTime >= wakeTime) {
        showTooltip(document.getElementById('sleep-time-hour'), '취침 시간은 기상 시간보다 빨라야 합니다.');
        // 적절한 기본 값으로 초기화
        document.getElementById('sleep-time-period').value = "PM";
        document.getElementById('sleep-time-hour').value = "10";
        document.getElementById('sleep-time-minute').value = "00";
    }
}
