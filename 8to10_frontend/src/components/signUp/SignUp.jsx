import { useState } from 'react';
import {
    validateNickname,
    validateEmail,
    validatePhone,
    validatePassword,
    validateName,
    validateConfirmationPassword
} from './validateSignUp';
import PublicHeader from "@/components/PublicHeader.jsx";

import "@/styles/signUp/SignUp.css";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {useNavigate} from "react-router-dom";

function SignUp() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        nickname: '',
        emailId: '',
        emailDomain: '',
        customDomain: '',
        phone: '',
        password1: '',
        password2: '',
        gender: '',
        userMode: '',
    });

    const [errors, setErrors] = useState({});
    const [isNicknameVerified, setNicknameVerified] = useState(false);
    const [isEmailVerified, setEmailVerified] = useState(false);

    const handleInputChange = (e) => {
        const { name, value } = e.target;

        setFormData({ ...formData, [name]: value });

        if (name === "name") {
            setErrors({ ...errors, name: validateName(value) });
        } else if (name === "phone") {
            setErrors({ ...errors, phone: validatePhone(value) });
        } else if (name === "password1") {
            setErrors({
                ...errors,
                password1: validatePassword(
                    name === "password1" ? value : formData.password1,
                ),
            });
        } else if (name === "password2") {
            setErrors({
                ...errors,
                password2: validateConfirmationPassword(
                    name === "password1" ? value : formData.password1,
                    name === "password2" ? value : formData.password2
                ),
            });
        }

        if (name === "nickname" && isNicknameVerified) {
            setNicknameVerified(false);
        } else if ((name === "emailId" || name === "emailDomain" || name === "customDomain") && isEmailVerified) {
            setEmailVerified(false);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const newErrors = {
            name: validateName(formData.name),
            phone: validatePhone(formData.phone),
            password: validatePassword(formData.password1, formData.password2),
            gender: formData.gender ? "" : "성별을 선택해주세요.",
            userMode: formData.userMode ? "" : "사용 모드를 선택해주세요.",
        };

        if (!isEmailVerified) {
            newErrors.email = "이메일 중복 확인이 필요합니다.";
        }
        if (!isNicknameVerified) {
            newErrors.nickname = "닉네임 중복 확인이 필요합니다.";
        }

        setErrors(newErrors);

        const hasErrors = Object.values(newErrors).some((error) => error);
        if (!hasErrors) {
            const finalData = {
                username: formData.name,
                nickname: formData.nickname,
                email: formData.emailId + '@' + (formData.emailDomain === 'custom' ? formData.customDomain : formData.emailDomain),
                password: formData.password1,
                phoneNumber: formData.phone,
                gender: formData.gender,
                mode: formData.userMode,
                isAuthEmail: true,
                isAuthPhone: true,
            };

            try {
                const url = "/signup";
                const response = await authenticatedApi.post(
                    url,
                    finalData,
                    {apiEndPoint: API_ENDPOINT_NAMES.SIGNUP,},
                );

                navigate("/signup/complete");
            } catch (error) {
                console.error(error.toString());
                console.error(error);
            }
        }
    };

    const handleNicknameVerification = async () => {
        const error = await validateNickname(formData.nickname);
        if (error) {
            setNicknameVerified(false);
            setErrors({ ...errors, nickname: error });
        } else {
            setNicknameVerified(true);
            setErrors({ ...errors, nickname: "" });
        }
    };

    const handleEmailVerification = async () => {
        const error = await validateEmail(formData.emailId, formData.emailDomain, formData.customDomain);
        if (error) {
            setEmailVerified(false);
            setErrors({ ...errors, email: error });
        } else {
            setEmailVerified(true);
            setErrors({ ...errors, email: "" });
        }
    };

    return (
        <>
            <PublicHeader />
            <div className="signup-container">
                <h2>회원가입</h2>
                <div className="signup-form">
                    {/* 이름 */}
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
                        {errors.name && <span className="signup-error">{errors.name}</span>}
                    </div>

                    {/* 닉네임 */}
                    <div className="signup-form-group">
                        <label htmlFor="nickname">닉네임</label>
                        <div className="nickname-container">
                            <input
                                type="text"
                                id="nickname"
                                name="nickname"
                                value={formData.nickname}
                                onChange={handleInputChange}
                                placeholder="닉네임"
                                required
                            />
                            <button
                                type="button"
                                className="check-duplicate-btn"
                                onClick={handleNicknameVerification}
                            >
                                중복 확인
                            </button>
                        </div>
                        {errors.nickname && <span className="signup-error">{errors.nickname}</span>}
                        {isNicknameVerified && <span className="signup-success">사용 가능한 닉네임입니다.</span>}
                    </div>

                    {/* 이메일 */}
                    <div className="signup-form-group signup-email-group">
                        <label htmlFor="email-id">이메일</label>
                        <div className="email-container">
                            <input
                                type="text"
                                id="email-id"
                                name="emailId"
                                value={formData.emailId}
                                onChange={handleInputChange}
                                placeholder="아이디"
                                required
                            />
                            <span className="email-separator">@</span>
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
                            <button
                                type="button"
                                className="check-duplicate-btn"
                                onClick={handleEmailVerification}
                            >
                                중복 확인
                            </button>
                        </div>
                        {errors.email && <span className="signup-error">{errors.email}</span>}
                        {isEmailVerified && <span className="signup-success">사용 가능한 이메일입니다.</span>}
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
                        {errors.password1 && <span className="signup-error">{errors.password1}</span>}
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
                        {errors.password2 && <span className="signup-error">{errors.password2}</span>}
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
                        {errors.phone && <span className="signup-error">{errors.phone}</span>}
                    </div>

                    {/* 성별 */}
                    <div className="signup-form-group">
                        <label htmlFor="gender">성별</label>
                        <div className="signup-radio-group" id="gender">
                            <label>
                                <input
                                    type="radio"
                                    name="gender"
                                    value="MALE"
                                    checked={formData.gender === "MALE"}
                                    onChange={handleInputChange}
                                />
                                남
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    name="gender"
                                    value="FEMALE"
                                    checked={formData.gender === "FEMALE"}
                                    onChange={handleInputChange}
                                />
                                여
                            </label>
                        </div>
                        {errors.gender && <span className="signup-error">{errors.gender}</span>}
                    </div>

                    {/* 사용 모드 */}
                    <div className="signup-form-group">
                        <label htmlFor="userMode">사용 모드</label>
                        <div className="signup-radio-group" id="userMode">
                            <label>
                                <input
                                    type="radio"
                                    name="userMode"
                                    value="MILD"
                                    checked={formData.userMode === "MILD"}
                                    onChange={handleInputChange}
                                />
                                Mild 모드
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    name="userMode"
                                    value="SPICY"
                                    checked={formData.userMode === "SPICY"}
                                    onChange={handleInputChange}
                                />
                                Spicy 모드
                            </label>
                        </div>
                        {errors.userMode && <span className="signup-error">{errors.userMode}</span>}
                    </div>

                    <button
                        className="signup-form-button"
                        onClick={handleSubmit}
                    >
                        회원가입
                    </button>
                </div>
            </div>
        </>
    );
}

export default SignUp;
