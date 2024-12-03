import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

export const validateName = (name) => {
    const NAME_PATTERN = /^[가-힣]{2,6}$/;

    if (!name || name.trim() === "") {
        return "이름을 입력해주세요.";
    }
    if (!NAME_PATTERN.test(name)) {
        return "이름은 한글 2~6글자여야 합니다.";
    }
    return null;
};

export const validateNickname = async (nickname) => {
    const NICKNAME_PATTERN = /^[가-힣a-zA-Z0-9_.]+$/;

    if (!nickname || nickname.trim() === "") {
        return "닉네임을 입력해주세요.";
    }

    if (!NICKNAME_PATTERN.test(nickname)) {
        return "닉네임은 한글, 영문, 숫자, '_', '.' 만 사용할 수 있습니다.";
    }

    if (/.*[가-힣]+.*/.test(nickname)) {
        if (nickname.length < 2 || nickname.length > 12) {
            return "한글 닉네임은 2~12자 이내여야 합니다.";
        }
    } else {
        if (nickname.length > 20) {
            return "영문 닉네임은 20자 이내여야 합니다.";
        }
    }

    const isDuplicated = await isDuplicatedNickname(nickname);
    if (isDuplicated) {
        return "사용중인 닉네임 입니다. 다른 닉네임을 입력해주세요."
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
    const domainRegex = /^(?!-)[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,63}$/;
    let email = emailId + '@' + (emailDomain === 'custom' ? customDomain : emailDomain);

    if (!emailId) {
        return "아이디를 입력해 주세요.";
    }

    if (!emailDomain || (emailDomain === "custom" && !customDomain)) {
        return "이메일 도메인을 입력해주세요.";
    }

    if (customDomain) {
        if (!domainRegex.test(customDomain)) {
            return "올바른 이메일 도메인을 입력해 주세요. 예: example.com";
        }
    }

    const isDuplicated = await isDuplicatedEmail(email);
    if (isDuplicated) {
        return "사용 중인 이메일입니다. 다른 이메일을 입력해주세요";
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
    const phoneRegex = /^(01[0-9])\d{7,8}$/;
    if (!phone) {
        return "전화번호를 입력해 주세요.";
    }
    if (!phoneRegex.test(phone)) {
        return "전화번호는 010~019로 시작하고, 10~11자리 숫자여야 합니다.";
    }

    return "";
}

export function validatePassword(password) {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$/;

    if (!password) {
        return "비밀번호를 입력해 주세요.";
    }

    if (!passwordRegex.test(password)) {
        return "비밀번호는 8~20자 사이여야 하며, 영문, 숫자, 특수문자(!@#$%^&*?_)를 포함해야 합니다.";
    }
    return "";
}

export function validateConfirmationPassword(password, confirmationPassword) {
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$/;

    if (!passwordRegex.test(confirmationPassword)) {
        return "비밀번호는 8~20자 사이여야 하며, 영문, 숫자, 특수문자(!@#$%^&*?_)를 포함해야 합니다.";
    }

    if (!confirmationPassword) {
        return "비밀번호 확인을 입력해 주세요.";
    }

    if (password !== confirmationPassword) {
        return "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
    }

    return "";
}