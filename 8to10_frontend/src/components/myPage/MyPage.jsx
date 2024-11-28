import {useState} from "react";

import "@/styles/myPage/MyPage.css";

function MyPage() {

    const [userNickname, setUserNickname] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [userRole, setUserRole] = useState("");
    const [userProfileImageUrl, setUserProfileImageUrl] = useState("");


    return (

        <div className="mypage-container">
            <div className="mypage-content">
                <div className="mypage-section">
                    <div className="section-header">
                        <h2>내 정보</h2>
                        <button className="logout-button">로그아웃</button>
                    </div>
                    <div className="profile-info">
                        <img src={userProfileImageUrl} alt="Profile Picture" className="profile-picture"/>
                        <div className="user-info">
                            <strong>{userNickname}</strong>
                            <span>{userRole} / {userEmail}</span>
                        </div>
                    </div>
                </div>
                <div className="mypage-section">
                    <h2>계정</h2>
                    <ul>
                        <li className="clickable-item">닉네임 변경</li>
                        <li className="clickable-item">비밀번호 변경</li>
                        <li className="clickable-item">프로필 사진 변경</li>
                    </ul>
                </div>
                <div className="mypage-section">
                    <h2>커뮤니티</h2>
                    <ul>
                        <li className="clickable-item">내가 쓴 게시글 보기</li>
                        <li className="clickable-item">내가 쓴 댓글 보기</li>
                        <li className="clickable-item">스크랩한 게시글 보기</li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default MyPage;
