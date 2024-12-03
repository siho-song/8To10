import {Link} from "react-router-dom";
import PublicHeader from "@/components/PublicHeader.jsx";
import "@/styles/signUp/SignUpComplete.css"

const SignUpComplete = () => {
    return (
        <>
            <PublicHeader />
            <div className="completion-container">
                <h2>회원가입 완료!</h2>
                <p>회원가입이 성공적으로 완료되었습니다. 이제 일정 관리 애플리케이션 EightToTen의 모든 기능을 자유롭게 이용할 수 있습니다.</p>
                <div className="link-to-login-btn">
                    <Link to="/" className="button">로그인하기</Link>
                </div>
            </div>
        </>
    );
}

export default SignUpComplete;