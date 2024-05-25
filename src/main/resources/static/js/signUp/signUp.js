let emailVerified = false;
let nicknameDuplicateChecked = false;
let emailDuplicateChecked = false;
let phoneDuplicateChecked = false;

function showEmailVerification() {
    document.getElementById('email-verification').style.display = 'block';
}

function verifyEmailCode() {
    const verificationCode = document.getElementById('email-verification-code').value;

    // 서버 요청을 시뮬레이션하는 코드
    if (verificationCode === "123456") { // 실제 구현에서는 서버로부터 검증 결과를 받아와야 합니다.
        emailVerified = true;
        document.getElementById('authEmail').value = 'true'; // 이메일 인증 성공 시 true 설정
        alert('이메일 인증되었습니다.');
    } else {
        alert('이메일 인증번호가 올바르지 않습니다.');
    }
}

function handleDomainChange() {
    const domainSelect = document.getElementById('email-domain');
    const customDomainInput = document.getElementById('custom-domain');
    if (domainSelect.value === "custom") {
        customDomainInput.style.display = 'inline-block';
        customDomainInput.required = true;
    } else {
        customDomainInput.style.display = 'none';
        customDomainInput.required = false;
    }
    validateEmail(); // 도메인 변경 시 이메일 유효성 검증
}

function validateNickname() {
    const nickname = document.getElementById('nickname').value;
    const messageSpan = document.getElementById('nickname-verification-message');

    if (nickname.length < 2 || nickname.length > 6) {
        messageSpan.textContent = "닉네임은 2~6자 사이여야 합니다.";
        messageSpan.className = "verification-message error";
        nicknameDuplicateChecked = false;
    } else {
        // 닉네임 중복 확인 서버 요청 시뮬레이션
        if (nickname === "testuser") { // 실제 구현에서는 서버로부터 검증 결과를 받아와야 합니다.
            messageSpan.textContent = "이미 사용 중인 닉네임입니다.";
            messageSpan.className = "verification-message error";
            nicknameDuplicateChecked = false;
        } else {
            messageSpan.textContent = "사용 가능한 닉네임입니다.";
            messageSpan.className = "verification-message success";
            nicknameDuplicateChecked = true;
        }
    }
}

function validateEmail() {
    const emailId = document.getElementById('email-id').value;
    const domainSelect = document.getElementById('email-domain');
    const customDomainInput = document.getElementById('custom-domain');
    let emailDomain = domainSelect.value;

    if (emailDomain === 'custom') {
        emailDomain = customDomainInput.value;
    }

    const email = emailId + '@' + emailDomain;
    const messageSpan = document.getElementById('email-verification-message');

    if (!emailId || !emailDomain || emailDomain === "") {
        messageSpan.textContent = "이메일을 입력해 주세요.";
        messageSpan.className = "verification-message error";
        emailDuplicateChecked = false;
    } else {
        // 이메일 중복 확인 서버 요청 시뮬레이션
        if (email === "test@example.com") { // 실제 구현에서는 서버로부터 검증 결과를 받아와야 합니다.
            messageSpan.textContent = "이미 사용 중인 이메일입니다.";
            messageSpan.className = "verification-message error";
            emailDuplicateChecked = false;
        } else {
            messageSpan.textContent = "사용 가능한 이메일입니다.";
            messageSpan.className = "verification-message success";
            emailDuplicateChecked = true;
        }
    }
}

function validatePhone() {
    const phone = document.getElementById('phone').value;
    const messageSpan = document.getElementById('phone-verification-message');

    if (!phone) {
        messageSpan.textContent = "전화번호를 입력해 주세요.";
        messageSpan.className = "verification-message error";
        phoneDuplicateChecked = false;
    } else if (!/^\d{10,11}$/.test(phone)) {
        messageSpan.textContent = "전화번호는 10자 또는 11자의 숫자만 입력할 수 있습니다.";
        messageSpan.className = "verification-message error";
        phoneDuplicateChecked = false;
    } else {
        // 전화번호 중복 확인 서버 요청 시뮬레이션
        if (phone === "01012345678") { // 실제 구현에서는 서버로부터 검증 결과를 받아와야 합니다.
            messageSpan.textContent = "이미 사용 중인 전화번호입니다.";
            messageSpan.className = "verification-message error";
            phoneDuplicateChecked = false;
        } else {
            messageSpan.textContent = "사용 가능한 전화번호입니다.";
            messageSpan.className = "verification-message success";
            phoneDuplicateChecked = true;
        }
    }
}

function validatePassword() {
    const password1 = document.getElementById('password1').value;
    const password2 = document.getElementById('password2').value;
    const passwordMessage = document.getElementById('password-requirements');

    const requirements = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$/;

    if (!requirements.test(password1)) {
        passwordMessage.textContent = "비밀번호는 영문자, 대문자, 숫자, 특수문자 중 3가지 이상 포함 , 8자 이상";
        passwordMessage.className = "verification-message error";
    } else {
        passwordMessage.textContent = "";
        passwordMessage.className = "";
    }

    if (password1 !== password2) {
        document.getElementById('password2-message').textContent = "비밀번호가 일치하지 않습니다.";
        document.getElementById('password2-message').className = "verification-message error";
    } else {
        document.getElementById('password2-message').textContent = "";
        document.getElementById('password2-message').className = "";
    }
}

function validateName() {
    const name = document.getElementById('name').value;
    const messageSpan = document.getElementById('name-verification-message');

    if (name.length < 2 || name.length > 6) {
        messageSpan.textContent = "이름은 2~6자 사이여야 합니다.";
        messageSpan.className = "verification-message error";
    } else {
        messageSpan.textContent = "";
        messageSpan.className = "";
    }
}

function preventWhitespace(event) {
    if (event.key === ' ') {
        event.preventDefault();
    }
}

function allowOnlyNumbers(event) {
    const key = event.key;
    if (!/[\d]/.test(key) && !['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(key)) {
        event.preventDefault();
    }
}

function allowOnlyAlphanumeric(event) {
    const key = event.key;
    if (!/[a-zA-Z0-9]/.test(key) && !['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'].includes(key)) {
        event.preventDefault();
    }
}

function removeWhitespaceAndDots(event) {
    const input = event.target;
    input.value = input.value.replace(/[\s·]/g, '');
}

function mergeEmailFields() {
    const emailId = document.getElementById('email-id').value;
    const domainSelect = document.getElementById('email-domain');
    const customDomainInput = document.getElementById('custom-domain');
    let emailDomain = domainSelect.value;

    if (emailDomain === 'custom') {
        emailDomain = customDomainInput.value;
    }

    const email = emailId + '@' + emailDomain;
    const emailField = document.getElementById('email');
    emailField.value = email;
}

document.querySelectorAll('input[type="text"], input[type="email"], input[type="password"], input[type="tel"]').forEach(input => {
    input.addEventListener('keydown', preventWhitespace);
    input.addEventListener('input', removeWhitespaceAndDots);
});

document.getElementById('phone').addEventListener('keydown', allowOnlyNumbers);
document.getElementById('email-id').addEventListener('keydown', allowOnlyAlphanumeric);

document.getElementById('name').addEventListener('input', validateName);
document.getElementById('nickname').addEventListener('input', validateNickname);
document.getElementById('email-id').addEventListener('input', validateEmail);
document.getElementById('custom-domain').addEventListener('input', validateEmail);
document.getElementById('email-domain').addEventListener('change', validateEmail);
document.getElementById('phone').addEventListener('input', validatePhone);
document.getElementById('password1').addEventListener('input', validatePassword);
document.getElementById('password2').addEventListener('input', validatePassword);

document.getElementById('signup-form').addEventListener('submit', function (event) {
    document.getElementById('authPhone').value = 'false'
    validateName();
    validateNickname();
    validateEmail();
    validatePhone();
    validatePassword();
    mergeEmailFields();

    if (document.querySelector('.verification-message.error')) {
        event.preventDefault(); // 폼 제출 방지
    }
});
