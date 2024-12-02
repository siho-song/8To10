import {useState} from "react";
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import PropTypes from "prop-types";
import {DEFAULT_PROFILE_IMAGE_PATH, USER_ROLE} from "@/constants/UserRole.js";

const ChangeProfileImage = ({userProfileImageUrl, changeProfileUrl, userRole, profileImageUpdated, changeProfileImageUpdated, onBack}) => {
    const [file, setFile] = useState(null);
    const [previewUrl, setPreviewUrl] = useState(userProfileImageUrl);

    const [isValidFile, setIsValidFile] = useState(true);
    const hasProfileImage = !Object.values(DEFAULT_PROFILE_IMAGE_PATH).includes(userProfileImageUrl);

    const handleFileChange = (e) => {
        const selectedFile = e.target.files[0];

        if (selectedFile) {
            if (!selectedFile.type.startsWith("image/")) {
                setIsValidFile(false);
                setFile(null);
                e.target.value = "";
                setPreviewUrl(null);
                return;
            }

            setFile(selectedFile);
            setIsValidFile(true);
            const fileUrl = URL.createObjectURL(selectedFile);
            setPreviewUrl(fileUrl);
        }
    };

    const handleProfileImageSubmit = async (e) => {
        e.preventDefault();

        if (!file) {
            alert("파일을 선택하세요.");
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        try {
            const url = "/mypage/profile/photo";
            const response = await authenticatedApi.put(
                url,
                formData,
                {apiEndPoint: API_ENDPOINT_NAMES.PUT_PROFILE_IMAGE,},
            );

            alert('프로필 사진이 업로드되었습니다.');
            changeProfileImageUpdated();
            onBack();
        } catch (error) {
            console.error(error.toString());
            console.error(error);
            alert('프로필 사진 업로드에 실패했습니다.');
        }
    };

    const handleProfileImageDelete = async (e) => {
        e.preventDefault();

        try {
            const url = "/mypage/profile/photo";
            const response = await authenticatedApi.delete(
                url,
                {apiEndPoint:API_ENDPOINT_NAMES.DELETE_PROFILE_IMAGE,},
            )
            if (userRole === USER_ROLE.ADMIN) {
                changeProfileUrl(DEFAULT_PROFILE_IMAGE_PATH.ADMIN);
            } else if (userRole === USER_ROLE.NORMAL_USER) {
                changeProfileUrl(DEFAULT_PROFILE_IMAGE_PATH.NORMAL_USER);
            } else if (userRole === USER_ROLE.FAITHFUL_USER) {
                changeProfileUrl(DEFAULT_PROFILE_IMAGE_PATH.FAITHFUL_USER);
            }

            alert('프로필 사진이 삭제 되었습니다.');
            changeProfileImageUpdated();
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
                    <ArrowBackIcon/> 뒤로
                </button>
                <h2>닉네임 변경</h2>
            </div>

            <div className="form-group">
                <h2>프로필 사진 업로드</h2>
                <div className="file-input-container">
                    <input
                        type="file"
                        onChange={handleFileChange}
                        className="file-input"
                    />
                </div>

                {previewUrl && (
                    <div className="preview-container">
                        <img
                            src={previewUrl}
                            alt="미리보기"
                            className="preview-image"
                        />
                    </div>
                )}
                {!isValidFile && <p className="error-message">이미지를 업로드할 수 없습니다. 파일 형식을 확인해주세요.</p>}
                <div className="profile-buttons">
                    <button
                        className="save-button"
                        disabled={!isValidFile}
                        onClick={handleProfileImageSubmit}>
                        저장
                    </button>
                    {hasProfileImage && <button
                        className="image-delete-btn"
                        onClick={handleProfileImageDelete}
                    >삭제</button>}
                </div>
            </div>
        </div>
    );
}

ChangeProfileImage.propTypes = {
    userProfileImageUrl: PropTypes.string,
    userRole: PropTypes.string,
    changeProfileUrl: PropTypes.func,
    onBack: PropTypes.func,
}

export default ChangeProfileImage;