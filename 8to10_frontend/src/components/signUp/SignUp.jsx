import { useState } from 'react';
import { validateNickname, validateEmail, validatePhone, validatePassword } from './validateSignUp'; // 유효성 검사 함수 가져오기

function SignUp() {
    const [formData, setFormData] = useState({
        name: '',
        nickname: '',
        emailId: '',
        emailDomain: '',
        customDomain: '',
        phone: '',
        password1: '',
        password2: '',
    });

    const [validationErrors, setValidationErrors] = useState({});

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        // 유효성 검사 실행
        const nicknameError = validateNickname(formData.nickname);
        const emailError = validateEmail(formData.emailId, formData.emailDomain, formData.customDomain);
        const phoneError = validatePhone(formData.phone);
        const passwordError = validatePassword(formData.password1, formData.password2);

        setValidationErrors({
            nickname: nicknameError,
            email: emailError,
            phone: phoneError,
            password: passwordError,
        });

        // 오류가 없으면 서버로 제출
        if (!nicknameError && !emailError && !phoneError && !passwordError) {
            // 서버로 폼 데이터 제출하는 로직
            console.log('회원가입 성공', formData);
        }
    };

    return (
        <div className="signup-container">
            <h2>회원가입</h2>
            <form onSubmit={handleSubmit}>
                {/* 이름 입력 */}
                <div className="signup-form-group">
                    <label htmlFor="name">이름</label>
                    <input
                        type="text"
                        id="name"
                        name="name"
                        value={formData.name}
                        onChange={handleInputChange}
                        placeholder="이름"
                        required
                    />
                </div>

                {/* 닉네임 */}
                <div className="signup-form-group">
                    <label htmlFor="nickname">닉네임</label>
                    <input
                        type="text"
                        id="nickname"
                        name="nickname"
                        value={formData.nickname}
                        onChange={handleInputChange}
                        placeholder="닉네임"
                        required
                    />
                    {validationErrors.nickname && <span className="error">{validationErrors.nickname}</span>}
                </div>

                {/* 이메일 */}
                <div className="signup-form-group">
                    <label htmlFor="email-id">이메일</label>
                    <input
                        type="text"
                        id="email-id"
                        name="emailId"
                        value={formData.emailId}
                        onChange={handleInputChange}
                        placeholder="아이디"
                        required
                    />
                    <select
                        id="email-domain"
                        name="emailDomain"
                        value={formData.emailDomain}
                        onChange={handleInputChange}
                        required
                    >
                        <option value="" disabled>선택하세요</option>
                        <option value="naver.com">naver.com</option>
                        <option value="google.com">google.com</option>
                        <option value="custom">직접 입력</option>
                    </select>
                    {formData.emailDomain === 'custom' && (
                        <input
                            type="text"
                            id="custom-domain"
                            name="customDomain"
                            value={formData.customDomain}
                            onChange={handleInputChange}
                            placeholder="직접 입력"
                            required
                        />
                    )}
                    {validationErrors.email && <span className="error">{validationErrors.email}</span>}
                </div>

                {/* 비밀번호 */}
                <div className="signup-form-group">
                    <label htmlFor="password1">비밀번호</label>
                    <input
                        type="password"
                        id="password1"
                        name="password1"
                        value={formData.password1}
                        onChange={handleInputChange}
                        placeholder="비밀번호"
                        required
                    />
                    {validationErrors.password && <span className="error">{validationErrors.password}</span>}
                </div>

                <div className="signup-form-group">
                    <label htmlFor="password2">비밀번호 확인</label>
                    <input
                        type="password"
                        id="password2"
                        name="password2"
                        value={formData.password2}
                        onChange={handleInputChange}
                        placeholder="비밀번호 확인"
                        required
                    />
                </div>

                {/* 전화번호 */}
                <div className="signup-form-group">
                    <label htmlFor="phone">전화번호</label>
                    <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value={formData.phone}
                        onChange={handleInputChange}
                        placeholder="전화번호"
                        required
                    />
                    {validationErrors.phone && <span className="error">{validationErrors.phone}</span>}
                </div>

                <button type="submit">회원가입</button>
            </form>
        </div>
    );
}

export default SignUp;
