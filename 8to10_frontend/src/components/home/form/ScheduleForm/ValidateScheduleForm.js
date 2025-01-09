import {
    DATE_RANGE_ERROR_MESSAGE,
    EVENT_CREATE_VALIDATE_MESSAGE,
    EVENT_DETAILS_VALIDATE_MESSAGE
} from "@/constants/ScheduleValidateMessage.js";
import {formatPeriodTimeToLocalTimeFormat} from "@/helpers/TimeFormatter.js";

export const validateTitle = (title, setErrorMessage) => {
    if (title.length < 1 || title.length > 80) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.TITLE_LENGTH);
        return false;
    }

    setErrorMessage("");
    return true;
};

const isValidDate = (d) => {
    const date = new Date(d);
    return !isNaN(date.getTime());
}

export const validateDate = (date, setErrorMessage) => {
    const dateToInt = parseInt(date.split('-')[0]);

    if (dateToInt < 1000) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.INVALID_DATE);
        return false;
    }

    if (!isValidDate(date)) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.DATE_EMPTY);
        return false;
    }

    const now = new Date();
    const tenYearsAgo = new Date(now.getFullYear() - 10, 0, 1);
    const tenYearsLater = new Date(now.getFullYear() + 10, 11, 31, 23, 59, 59);

    const target = new Date(date);

    if (target < tenYearsAgo || target > tenYearsLater) {
        setErrorMessage(DATE_RANGE_ERROR_MESSAGE(tenYearsAgo.getFullYear(), tenYearsLater.getFullYear()));
        return false;
    }

    setErrorMessage("");
    return true;
};

export const isStartDateBeforeEndDate = (startDate, endDate, setErrorMessage) => {
    const start = new Date(startDate);
    const end = new Date(endDate);
    if (!(start < end)) {
        setErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.DATE);
        return false;
    }
    setErrorMessage("");
    return true;
};

export const isStartDateTimeBeforeEndDateTime = (startDateTime, endDateTime, setErrorMessage) => {
    const {startDate, startTime, startHour, startMinute} = startDateTime;
    const {endDate, endTime, endHour, endMinute} = endDateTime;

    const s = startDate + "T" + formatPeriodTimeToLocalTimeFormat(startTime, startHour, startMinute);
    const e = endDate + "T" + formatPeriodTimeToLocalTimeFormat(endTime, endHour, endMinute);

    const start = new Date(s);
    const end = new Date(e);

    if (!(start < end)) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.DATE_VARIABLE);
        return false;
    }

    setErrorMessage("");
    return true;
};

export const validateDurationTime = (durationHour, durationMinute, setErrorMessage) => {
    if (durationHour === "00" && durationMinute === "00") {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.DURATION_TIME);
        return false;
    }

    setErrorMessage("");
    return true;
};

export const validatePerformTime = (performHour, performMinute, setErrorMessage) => {
    if (performHour === "00" && performMinute === "00") {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.PERFORM_IN_DAY);
        return false;
    }

    setErrorMessage("");
    return true;
};

const sumTimes = (bufferHour, bufferMinute, performHour, performMinute) => {
    const bufferTotalMinutes = bufferHour * 60 + bufferMinute;
    const performTotalMinutes = performHour * 60 + performMinute;

    const totalMinutes = bufferTotalMinutes + performTotalMinutes;

    const hour = Math.floor(totalMinutes / 60);
    const minute = totalMinutes % 60;

    return { hour, minute };
}

export const validateBufferPerformSum = (bufferHour, bufferMinute, performHour, performMinute, setErrorMessage) => {
    const bh = parseInt(bufferHour);
    const bm = parseInt(bufferMinute);
    const ph = parseInt(performHour);
    const pm = parseInt(performMinute);

    const {hour, minute} = sumTimes(bh, bm, ph, pm);

    if (hour > 14 || (hour === 14 && minute > 0)) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.BUFFER_PERFORM_SUM);
        return false;
    }

    setErrorMessage("");
    return true;
}

export const validateDayChecked = (days, setErrorMessage) => {
    if (days.length === 0) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.DAY_UNCHECKED);
        return false;
    }
    setErrorMessage("");
    return true;
}

export const validatePerformInWeek = (performInWeek, setErrorMessage) => {
    if (!performInWeek) {
        setErrorMessage(EVENT_CREATE_VALIDATE_MESSAGE.PERFORM_IN_WEEK);
        return false;
    }

    setErrorMessage("");
    return true;
}

export const validateTotalAmount = (value) => {
    const numericRegex = /^\d*$/;
    return numericRegex.test(value);
}