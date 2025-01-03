import {DATE_RANGE_ERROR_MESSAGE, EVENT_DETAILS_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";
import {createLocalDateTime} from "@/helpers/TimeFormatter.js";

export const validateTitle = (title, setErrorMessage) => {
    if(title.length < 1 || title.length > 80) {
        setErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.TITLE_LENGTH);
        return false;
    }
    setErrorMessage("");
    return true;
}

export const isStartDateBeforeEndDate = (startDateTime, endDateTime, setDateErrorMessage) => {
    const start = new Date(startDateTime);
    const end = new Date(endDateTime);
    if (!(start < end)) {
        setDateErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.DATE);
        return false;
    }
    setDateErrorMessage("");
    return true;
}

export const validateDateInput = (date, setDateErrorMessage) => {
    const isFieldEmpty = ({ date, period, hour, minute }) =>
        !date || !period || hour == null || minute == null;

    const dateToInt = parseInt(date.date.split('-')[0]);

    if (dateToInt < 1000) {
        setDateErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.INVALID_DATE);
        return false;
    }

    if (isFieldEmpty(date)) {
        setDateErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.DATE_EMPTY);
        return false;
    }

    const dateTime = createLocalDateTime(date);

    const now = new Date();
    const tenYearsAgo = new Date(now.getFullYear() - 10, 0, 1);
    const tenYearsLater = new Date(now.getFullYear() + 10, 11, 31, 23, 59, 59);

    const target = new Date(dateTime);

    if (target < tenYearsAgo || target > tenYearsLater) {
        setDateErrorMessage(DATE_RANGE_ERROR_MESSAGE(tenYearsAgo.getFullYear(), tenYearsLater.getFullYear()));
        return false;
    }

    setDateErrorMessage("");
    return true;
};