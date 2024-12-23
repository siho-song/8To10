import {EVENT_DETAILS_VALIDATE_MESSAGE} from "@/constants/ScheduleValidateMessage.js";

export const validateTitle = (title, setErrorMessage) => {
    if(title.length < 1 || title.length > 80) {
        setErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.TITLE_LENGTH);
        return false;
    }
    setErrorMessage("");
    return true;
}

export const validateDateTime = (startDateTime, endDateTime, setDateErrorMessage) => {
    const start = new Date(startDateTime);
    const end = new Date(endDateTime);
    if (!(start <= end)) {
        setDateErrorMessage(EVENT_DETAILS_VALIDATE_MESSAGE.DATE);
        return false;
    }
    setDateErrorMessage("");
    return true;
}