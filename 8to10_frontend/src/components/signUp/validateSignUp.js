import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {
    EMAIL_FIELD_MESSAGE,
    EMAIL_PATTERN,
    NAME_FIELD_MESSAGE,
    NAME_PATTERN, NICKNAME_FIELD_MESSAGE,
    NICKNAME_PATTERN, PASSWORD_FIELD_MESSAGE, PASSWORD_PATTERN, PHONE_NUMBER_FIELD_MESSAGE,
    PHONE_NUMBER_PATTERN
} from "@/constants/SignUpValidateMessage.js";

export const validateName = (name) => {

    if (!name || name.trim() === "") {
        return NAME_FIELD_MESSAGE.EMPTY;
    }
    if (!NAME_PATTERN.test(name)) {
        return NAME_FIELD_MESSAGE.INVALID_VALUE;
    }
    return null;
};

export const validateNickname = async (nickname) => {
    if (!nickname || nickname.trim() === "") {
        return NICKNAME_FIELD_MESSAGE.EMPTY;
    }

    if (!NICKNAME_PATTERN.test(nickname)) {
        return NICKNAME_FIELD_MESSAGE.INVALID_VALUE;
    }

    if (/.*[가-힣]+.*/.test(nickname)) {
        if (nickname.length < 2 || nickname.length > 12) {
            return NICKNAME_FIELD_MESSAGE.KOREAN_LENGTH_CONSTRAINT;
        }
    } else {
        if (nickname.length > 20) {
            return NICKNAME_FIELD_MESSAGE.LENGTH_CONSTRAINT;
        }
    }

    const isDuplicated = await isDuplicatedNickname(nickname);
    if (isDuplicated) {
        return NICKNAME_FIELD_MESSAGE.DUPLICATED;
    } else if (isDuplicated === undefined) {
        return NICKNAME_FIELD_MESSAGE.NETWORK_ERROR;
    }
    return null;
};

export const isDuplicatedNickname = async (nickname) => {
    try {
        const url = `/signup/nickname/exists?nickname=${nickname}`;
        const response = await authenticatedApi.get(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.SIGNUP_NICKNAME_EXISTS,},
        );

        return response.data;
    } catch (error) {
        console.error(error.toString());
        console.error(error);
    }
}

export const validateEmail = async (emailId, emailDomain, customDomain = "") => {
    let email = emailId + '@' + (emailDomain === 'custom' ? customDomain : emailDomain);

    if (!emailId) {
        return EMAIL_FIELD_MESSAGE.EMAIL_ID_EMPTY;
    }

    if (!emailDomain || (emailDomain === "custom" && !customDomain)) {
        return EMAIL_FIELD_MESSAGE.EMAIL_DOMAIN_EMPTY;
    }

    if (customDomain) {
        if (!EMAIL_PATTERN.test(customDomain)) {
            return EMAIL_FIELD_MESSAGE.EMAIL_INVALID_DOMAIN;
        }
    }

    const isDuplicated = await isDuplicatedEmail(email);
    if (isDuplicated) {
        return EMAIL_FIELD_MESSAGE.DUPLICATED;
    } else if (isDuplicated === undefined) {
        return EMAIL_FIELD_MESSAGE.NETWORK_ERROR;
    }

    return "";
}

const isDuplicatedEmail = async (email) => {
    try {
        const url = `/signup/email/exists?email=${email}`;
        const response = await authenticatedApi.get(
            url,
            {apiEndPoint: API_ENDPOINT_NAMES.SIGNUP_EMAIL_EXISTS},
        )

        return response.data;
    } catch (error) {
        console.error(error.toString());
        console.error(error);
    }
}

export function validatePhone(phone) {
    if (!phone) {
        return PHONE_NUMBER_FIELD_MESSAGE.EMPTY;
    }
    if (!PHONE_NUMBER_PATTERN.test(phone)) {
        return PHONE_NUMBER_FIELD_MESSAGE.INVALID_VALUE;
    }

    return "";
}

export function validatePassword(password) {

    if (!password) {
        return PASSWORD_FIELD_MESSAGE.EMPTY;
    }

    if (!PASSWORD_PATTERN.test(password)) {
        return PASSWORD_FIELD_MESSAGE.INVALID_VALUE;
    }
    return "";
}

export function validateConfirmationPassword(password, confirmationPassword) {

    if (!PASSWORD_PATTERN.test(confirmationPassword)) {
        return PASSWORD_FIELD_MESSAGE.INVALID_VALUE;
    }

    if (!confirmationPassword) {
        return PASSWORD_FIELD_MESSAGE.EMPTY_CONFIRMATION_PASSWORD;
    }

    if (password !== confirmationPassword) {
        return PASSWORD_FIELD_MESSAGE.CONFIRMATION_PASSWORD_MISMATCH;
    }

    return "";
}

export function validateSubmit() {

}