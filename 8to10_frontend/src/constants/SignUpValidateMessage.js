export const NAME_PATTERN = /^[가-힣]{2,6}$/;
export const NICKNAME_PATTERN = /^[가-힣a-zA-Z0-9_.]+$/;
export const EMAIL_PATTERN = /^(?!-)[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,63}$/;
export const PHONE_NUMBER_PATTERN = /^(01[0-9])\d{7,8}$/;
export const PASSWORD_PATTERN = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$/;



export const NAME_FIELD_MESSAGE = {
    EMPTY: "이름을 입력해주세요.",
    INVALID_VALUE: "이름은 한글 2~6글자여야 합니다.",
}

export const NICKNAME_FIELD_MESSAGE = {
    EMPTY: "닉네임을 입력해주세요.",
    INVALID_VALUE: "닉네임은 한글, 영문, 숫자, '_', '.' 만 사용할 수 있습니다.",
    KOREAN_LENGTH_CONSTRAINT: "한글 닉네임은 2~12자 이내여야 합니다.",
    LENGTH_CONSTRAINT: "영문 닉네임은 20자 이내여야 합니다.",
    DUPLICATED: "사용중인 닉네임 입니다. 다른 닉네임을 입력해주세요.",
    NETWORK_ERROR: "현재 네트워크 상태가 원활하지 않아 닉네임 중복 확인을 할 수 없습니다. 잠시 후 다시 시도해주세요."
}

export const EMAIL_FIELD_MESSAGE = {
    EMAIL_ID_EMPTY: "아이디를 입력해 주세요.",
    EMAIL_DOMAIN_EMPTY: "이메일 도메인을 입력해주세요.",
    EMAIL_INVALID_DOMAIN: "올바른 이메일 도메인을 입력해 주세요. 예: example.com",
    DUPLICATED: "사용 중인 이메일입니다. 다른 이메일을 입력해주세요",
    NETWORK_ERROR: "현재 네트워크 상태가 원활하지 않아 이메일 중복 확인을 할 수 없습니다. 잠시 후 다시 시도해주세요."
}

export const PHONE_NUMBER_FIELD_MESSAGE = {
    EMPTY: "전화번호를 입력해 주세요.",
    INVALID_VALUE: "전화번호는 010~019로 시작하고, 10~11자리 숫자여야 합니다."
}

export const PASSWORD_FIELD_MESSAGE = {
    EMPTY: "비밀번호를 입력해 주세요.",
    INVALID_VALUE: "비밀번호는 8~20자 사이여야 하며, 영문, 숫자, 특수문자(!@#$%^&*?_)를 포함해야 합니다.",
    EMPTY_CONFIRMATION_PASSWORD: "비밀번호 확인을 입력해 주세요.",
    CONFIRMATION_PASSWORD_MISMATCH: "비밀번호와 비밀번호 확인이 일치하지 않습니다."
}

export const SUBMIT_MESSAGE = {
    ERROR: "회원가입에 실패했습니다. 입력 정보를 확인한 후 다시 시도해주세요.",
}