export const EVENT_DETAILS_VALIDATE_MESSAGE = {
    TITLE_LENGTH: "제목은 최소 1자 이상 최대 80자 이하이어야 합니다.",
    DATE: "시작 날짜는 종료 날짜보다 이전 이어야 합니다.",
    DATE_EMPTY: "모든 날짜와 시간이 입력되어야 합니다.",
    INVALID_DATE: "입력한 날짜의 연도가 너무 과거입니다. 유효한 날짜를 입력해주세요.",
}

export const EVENT_CREATE_VALIDATE_MESSAGE = {
    TITLE_LENGTH: "제목은 최소 1자 이상 최대 80자 이하이어야 합니다.",
    DURATION_TIME: "지속 시간은 0시간 0분을 초과해야 합니다.",
    DATE: "시작 날짜는 종료 날짜 이전이어야 합니다.",
    DATE_VARIABLE: "시작 날짜는 종료 날짜와 같거나 이전이어야 합니다.",
    DATE_EMPTY: "모든 날짜와 시간이 입력되어야 합니다.",
    INVALID_DATE: "입력한 날짜의 연도가 너무 과거입니다. 유효한 날짜를 입력해주세요.",
    DAY_UNCHECKED: "최소 하나의 수행 요일을 선택해야 합니다.",
    PERFORM_IN_DAY: "희망 하루 수행시간은 0시간 0분을 초과해야 합니다.",
    SELECT_WEEKEND: "희망 주간 수행빈도가 6회인 경우 최소 하나의 요일을 선택해야 합니다.",
    SUBMIT: "겹치는 일정이 있어 일정을 생성할 수 없습니다. 일정을 확인한 후 다시 일정을 생성해주세요.",
}

export const DATE_RANGE_ERROR_MESSAGE = (startYear, endYear) =>
    `날짜는 ${startYear}년 1월 1일부터 ${endYear}년 12월 31일 사이여야 합니다.`;
