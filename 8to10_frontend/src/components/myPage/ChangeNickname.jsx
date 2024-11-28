import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import PropTypes from "prop-types";

function ChangeNickname({ nickname, changeNickname, onBack }) {

    const [newNickname, setNewNickname] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [submitError, setSubmitError] = useState("");
    const [isDuplicate, setIsDuplicate] = useState(null);

    const [nonDuplicatedMessage, setNonDuplicatedMessage] = useState("");
    let validNickname = "";

    const isNicknameValid = (nickname) => {
        const regex = /^(?=.{2,12}$|^[a-zA-Z0-9_.]{2,20}$)[ㄱ-ㅎ가-힣a-zA-Z0-9_.]+$/;
        return regex.test(nickname);
    };

    const handleNewNicknameInput = (e) => {
        const inputValue = e.target.value;
        setNewNickname(inputValue);

        if (!isNicknameValid(inputValue)) {
            setErrorMessage(
                "닉네임은 2~12자의 한글 또는 20자의 영어, 숫자, 특수문자(_와 .만 허용)만 가능합니다."
            );
        } else {
            setErrorMessage("");
        }

        if (newNickname !== validNickname) {
            setIsDuplicate(null);
            setNonDuplicatedMessage("");
        }
    };

    const handleCheckDuplicate = async () => {

        try {
            const url = `/signup/nickname/exists?nickname=${newNickname}`;
            const response = await authenticatedApi.get(
                url,
                {apiEndPoint:API_ENDPOINT_NAMES.SIGNUP_NICKNAME_EXISTS,}
            );

            console.log(response.data);
            setIsDuplicate(false);
            setNonDuplicatedMessage("사용할 수 있는 닉네임입니다.");
            validNickname = newNickname;
        } catch (error) {
            setIsDuplicate(true);
            setErrorMessage("이미 사용 중인 닉네임입니다. 다른 닉네임을 입력해주세요.");
            validNickname="";
            // console.log(error.toString());
            // console.log(error);
        }
    };

    const handleNewNicknameSubmit = async () => {
        if (!isNicknameValid(newNickname)) {
            setSubmitError("닉네임 조건을 충족하지 못했습니다. 다시 입력해주세요.");
            return;
        }

        if (isDuplicate === null) {
            setSubmitError("닉네임 중복확인을 해주세요.");
            return;
        }

        try {
            const url = "/mypage/account/nickname";
            const response = await authenticatedApi.put(
                url,
                {nickname: newNickname,},
                {apiEndPoint: API_ENDPOINT_NAMES.PUT_USER_NICKNAME,}
            );

            changeNickname(newNickname);
            onBack();
        } catch (error) {
            setSubmitError("서버 오류가 발생했습니다. 다시 시도해주세요.");
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
                <h2>닉네임 변경</h2>
            </div>

            <div className="form-group">
                <label htmlFor="nickname">새 닉네임</label>
                <div className="nickname-input-group">
                    <input
                        type="text"
                        id="nickname"
                        name="nickname"
                        value={newNickname}
                        onChange={handleNewNicknameInput}
                        placeholder={nickname}
                    />
                    <button
                        type="button"
                        className="check-duplicate-button"
                        onClick={handleCheckDuplicate}
                        disabled={newNickname.length === 0 || errorMessage.length > 0 || isDuplicate===false}
                    >
                        중복확인
                    </button>
                </div>
                {nonDuplicatedMessage && <p className="non-duplicated-message">{nonDuplicatedMessage}</p>}
                {errorMessage && <p className="error-message">{errorMessage}</p>}
            </div>
            <button
                className="save-button"
                disabled={newNickname.length === 0 || errorMessage.length > 0 || isDuplicate===true}
                onClick={handleNewNicknameSubmit}
            >
                저장
            </button>
            {submitError && <p className="submit-error-message">{submitError}</p>}
        </div>
    );
}

ChangeNickname.propTypes = {
    nickname: PropTypes.string.isRequired,
    changeNickname: PropTypes.func.isRequired,
    onBack: PropTypes.func.isRequired,
}

export default ChangeNickname;
