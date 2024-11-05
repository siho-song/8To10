export function convertTo24HourFormat(date, hour, minute) {
    const hourInt = parseInt(hour) % 12 + (hour >= 12 ? 12 : 0); // 24시간 형식으로 변환
    return `${date}T${hourInt.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00`;
}

export const convertToDuration = (hour, minute) => {
    return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
};

export const convertPeriodTimeToLocalTimeFormat = (period, hour, minute) => {
    console.log("hour : ", hour);
    if (period === 'PM' && hour !== 12) {
        hour += 12;
    } else if (period === 'AM' && hour === 12) {
        hour = 0;
    }

    console.log("LocalTime : ", `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`);
    return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
};

export function formatDateTime(dateTime) {
    const date = new Date(dateTime);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    let hours = date.getHours();
    const minutes = String(date.getMinutes()).padStart(2, '0');

    const ampm = hours >= 12 ? '오후' : '오전'; // AM/PM 결정
    hours = hours % 12; // 12시간제로 변환
    hours = hours ? String(hours).padStart(2, '0') : '12'; // 0일 경우 12로 표시

    return `${year}년 ${month}월 ${day}일 ${ampm} ${hours}시 ${minutes}분`;
}
