import {useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import PropTypes from "prop-types";

const ChangePassword = ({onBack}) => {

    const [newPassword, setNewPassword] = useState("");
    const [confirmationPassword, setConfirmationPassword] = useState("");
    const [submitError, setSubmitError] = useState("");

    const [isSamePassword, setIsSamePassword] = useState(false);
    const [isValidPassword, setIsValidPassword] = useState(false);

    const validatePasswordPattern = (password) => {
        const regex = /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,20}$/;
        return regex.test(password);
    };

    const validatePassword = () => {

        if (!isValidPassword) {
            setSubmitError("비밀번호가 유효하지 않습니다. 비밀번호의 조합을 확인해주세요");
            return false;
        }
        if (!isSamePassword) {
            setSubmitError("비밀번호가 일치하지 않습니다.");
            return false;
        }

        return true;
    }

    const handleNewPasswordInput = (e) => {
        const inputValue = e.target.value;
        setNewPassword(inputValue);

        if (validatePasswordPattern(inputValue)) {
            setIsValidPassword(true);
        } else {
            setIsValidPassword(false);
        }
    }

    const handleConfirmationPasswordInput = (e) => {
        const inputValue = e.target.value;
        setConfirmationPassword(inputValue);
        setIsSamePassword(inputValue === newPassword);
    }

    const handleNewPasswordSubmit = async () => {
        if (!validatePassword()) return;

        try {
            const url = "/mypage/account/password";
            const response = await authenticatedApi.put(
                url,
                {password: newPassword},
                {apiEndPoint: API_ENDPOINT_NAMES.PUT_USER_PASSWORD,},
            );

            onBack();
        } catch (error) {
            console.error(error.toString());
            console.error(error);
        }
    }
    return (
        <div className="mypage-section">
            <div className="section-header">
                <button className="back-button" onClick={onBack}>
                    <ArrowBackIcon /> 뒤로
                </button>
                <h2>비밀번호 변경</h2>
            </div>
            <p className="password-inform">영문자, 숫자, 특수문자 3가지 이상 조합으로 8~20자 사이의 비밀번호를 입력해주세요.</p>
            <div className="form-group">
                <label htmlFor="password">새로운 비밀번호</label>
                <div className="password-input-group">
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={newPassword}
                        onChange={handleNewPasswordInput}
                        maxLength={20}
                        placeholder={"비밀번호"}
                    />
                </div>
                <div className="password-input-group">
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={confirmationPassword}
                        onChange={handleConfirmationPasswordInput}
                        maxLength={20}
                        placeholder={"비밀번호 확인"}
                    />
                </div>
            </div>
            {!isValidPassword && newPassword.length > 0 && (
                <p className="error-message">비밀번호 조건에 맞지 않습니다.</p>
            )}
            {!isSamePassword && confirmationPassword.length > 0 && (
                <p className="error-message">비밀번호가 일치하지 않습니다.</p>
            )}
            <button
                className="save-button"
                disabled={!isSamePassword || !isValidPassword}
                onClick={handleNewPasswordSubmit}
            >
                저장
            </button>
            {submitError && <p className="submit-error-message">{submitError}</p>}
        </div>
    );
}

ChangePassword.propTypes = {
    onBack: PropTypes.func.isRequired,
}

export default ChangePassword;