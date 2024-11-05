// validateSignUp.js

// 닉네임 유효성 검사
export function validateNickname(nickname) {
    if (nickname.length < 2 || nickname.length > 6) {
        return "닉네임은 2~6자 사이여야 합니다.";
    }
    // 중복 닉네임 체크 (서버 요청 로직으로 변경 가능)
    if (nickname === "testuser") {
        return "이미 사용 중인 닉네임입니다.";
    }
    return ""; // 유효성 검사 통과
}

// 이메일 유효성 검사
export function validateEmail(emailId, emailDomain, customDomain = "") {
    let email = emailId + '@' + (emailDomain === 'custom' ? customDomain : emailDomain);
    if (!emailId || !emailDomain) {
        return "이메일을 입력해 주세요.";
    }
    // 중복 이메일 체크 (서버 요청 로직으로 변경 가능)
    if (email === "test@example.com") {
        return "이미 사용 중인 이메일입니다.";
    }
    return ""; // 유효성 검사 통과
}

// 전화번호 유효성 검사
export function validatePhone(phone) {
    const phoneRegex = /^\d{10,11}$/;
    if (!phone) {
        return "전화번호를 입력해 주세요.";
    }
    if (!phoneRegex.test(phone)) {
        return "전화번호는 10~11자리 숫자여야 합니다.";
    }
    // 중복 전화번호 체크 (서버 요청 로직으로 변경 가능)
    if (phone === "01012345678") {
        return "이미 사용 중인 전화번호입니다.";
    }
    return ""; // 유효성 검사 통과
}

// 비밀번호 유효성 검사
export function validatePassword(password1, password2) {
    const passwordRequirements = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;
    if (!passwordRequirements.test(password1)) {
        return "비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다.";
    }
    if (password1 !== password2) {
        return "비밀번호가 일치하지 않습니다.";
    }
    return ""; // 유효성 검사 통과
}
