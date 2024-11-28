function MyPage() {

    return (

        <div className="mypage-container">
            <div className="mypage-content">
                <div className="mypage-section">
                    <div className="section-header">
                        <h2>내 정보</h2>
                        <button className="logout-button">로그아웃</button>
                    </div>
                    <div className="profile-info">
                        <img src="https://via.placeholder.com/100" alt="Profile Picture" className="profile-picture"/>
                        <div className="user-info">
                            <strong>이름 / 닉네임</strong>
                            <span>아이디</span>
                        </div>
                    </div>
                </div>
                <div className="mypage-section">
                    <h2>계정</h2>
                    <ul>
                        <li className="clickable-item" onClick="location.href='/mypage/change-pages/change-email'">이메일 변경</li>
                        <li className="clickable-item" onClick="location.href='/mypage/change-pages/change-phone'">휴대폰 번호 변경</li>
                        <li className="clickable-item" onClick="location.href='/mypage/change-pages/change-nickname'">닉네임 변경</li>
                        <li className="clickable-item" onClick="location.href='/mypage/change-pages/change-password'">비밀번호 변경</li>
                    </ul>
                </div>
                <div className="mypage-section">
                    <h2>커뮤니티</h2>
                    <ul>
                        <li className="clickable-item" onClick="location.href='/mypage/my-posts'">내가 쓴 게시글 보기</li>
                        <li className="clickable-item" onClick="location.href='/mypage/my-comments'">댓글 쓴 글보기</li>
                        <li className="clickable-item" onClick="location.href='/mypage/my-scrap'">스크랩한 게시글 보기</li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default MyPage;