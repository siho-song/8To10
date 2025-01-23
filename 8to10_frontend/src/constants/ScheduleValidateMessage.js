export const EVENT_DETAILS_VALIDATE_MESSAGE = {
    TITLE_LENGTH: "제목은 최소 1자 이상 최대 80자 이하이어야 합니다.",
    DATE: "시작 날짜는 종료 날짜보다 이전 이어야 합니다.",
    DATE_EMPTY: "모든 날짜와 시간이 입력되어야 합니다.",
    INVALID_DATE: "유효한 날짜를 입력해주세요.",
    MODIFICATION_SUCCESS: "일정을 성공적으로 수정했습니다",
    MODIFICATION_FAIL: "일정을 수정하지 못했습니다. 다시시도 해주세요.",
    MEMO_SUCCESS: "메모를 성공적으로 수정했습니다.",
    MEMO_FAIL: "메모를 삭제하지 못했습니다. 다시시도 해주세요.",
    DELETE_SUCCESS: "일정을 성공적으로 삭제했습니다.",
    DELETE_FAIL: "일정을 삭제하지 못했습니다. 다시시도 해주세요.",
}

export const EVENT_CREATE_VALIDATE_MESSAGE = {
    TITLE_LENGTH: "제목은 최소 1자 이상 최대 80자 이하이어야 합니다.",
    DURATION_TIME: "지속 시간은 최소 1분 이상이어야 합니다.",
    DATE: "시작 날짜는 종료 날짜 이전이어야 합니다.",
    DATE_VARIABLE: "시작 날짜는 종료 날짜와 같거나 이전이어야 합니다.",
    DATE_EMPTY: "모든 날짜(년, 월, 일)가 입력되어야 합니다.",
    INVALID_DATE: "유효한 날짜를 입력해주세요.",
    DAY_UNCHECKED: "최소 하나의 수행 요일을 선택해야 합니다.",
    PERFORM_IN_DAY: "희망 하루 수행시간은 최소 1분 이상이어야 합니다.",
    BUFFER_PERFORM_SUM: "희망 여유 시간과 희망 하루 수행시간을 합쳐서 14시간을 초과할 수 없습니다.",
    SELECT_WEEKEND: "희망 주간 수행빈도가 6회인 경우 최소 하나의 요일을 선택해야 합니다.",
    PERFORM_IN_WEEK: "희망 주간 수행 빈도가 비어있습니다. 주말 포함 여부를 체크하고 빈도를 설정해주세요.",
    PERFORM_IN_WEEK_WITHOUT_SELECT_WEEKEND: "주말이 포함되지 않으면 주간 수행 빈도는 최대 5회까지만 설정 가능합니다.",
    PERFORM_IN_WEEK_WITHOUT_ALL_SELECT_WEEKEND: "주말에 하루만 포함될 경우 주간 수행 빈도는 최대 6회까지만 설정 가능합니다.",
    SUBMIT_NORMAL: "겹치는 일정이 있어 일정을 생성할 수 없습니다. 일정을 확인한 후 다시 일정을 생성해주세요.",
    SUBMIT_SUCCESS: "일정이 성공적으로 생성되었습니다.",
    SUBMIT: "일정 생성에 실패했습니다. 일정을 확인한 후 다시 생성해주세요.",
}

export const DATE_RANGE_ERROR_MESSAGE = (startYear, endYear) =>
    `날짜는 ${startYear}년 1월 1일부터 ${endYear}년 12월 31일 사이여야 합니다.`;
