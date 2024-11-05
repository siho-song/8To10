import { useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/components/context/UseAuth.jsx';

import "@/styles/login/Login.css";

function Login() {

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { loginUser, errorMessage } = useAuth();

    const {isAuthenticated} = useAuth();

    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        loginUser(email, password);
    };

    useEffect(() => {
        if (isAuthenticated) {
            navigate('/home');
        }
    }, [isAuthenticated]);

    return (
        <div className="login-container">
            <h2>로그인</h2>
            <form onSubmit={handleSubmit} method="POST">
                <div className="form-group">
                    <label htmlFor="email">이메일</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="password">비밀번호</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                {errorMessage && <p className="error-message">{errorMessage}</p>}
                <div className="button-group">
                    <button type="submit" className="button login-button">
                        로그인
                    </button>
                    <button
                        type="button"
                        className="button button-cancel login-button"
                        onClick={() => navigate('/signup')}
                    >
                        회원가입
                    </button>
                </div>
                <div className="social-login">
                    <button className="kakao-login">카카오 로그인</button>
                    <button className="google-login">구글 로그인</button>
                    <button className="naver-login">네이버 로그인</button>
                </div>
            </form>
        </div>
    );
}

export default Login;
//
//
// function Login() {
//     const [email, setEmail] = useState('');
//     const [password, setPassword] = useState('');
//     const [isAuthenticated, setIsAuthenticated] = useState(false); // 인증 상태 관리
//     const [errorMessage, setErrorMessage] = useState(''); // 에러 메시지 관리
//     const navigate = useNavigate(); // 클라이언트 사이드 라우팅을 위한 hook
//
//     const handleSubmit = async (e) => {
//         e.preventDefault(); // 기본 폼 제출 동작을 막음
//
//         const requestData = new URLSearchParams({ email, password });
//
//         try {
//             const response = await fetch(`/api/login?${requestData.toString()}`, {
//                 method: 'POST',
//                 headers: { 'Content-Type': 'application/json' },
//                 credentials: 'include', // 서버에 쿠키 포함 요청
//             });
//
//             if (response.ok) {
//                 setIsAuthenticated(true); // 로그인 성공 상태 업데이트
//                 setErrorMessage(''); // 에러 메시지 초기화
//                 console.log('로그인 성공');
//                 navigate('/home'); // 성공 시 /home으로 이동
//             } else {
//                 const errorData = await response.json();
//                 console.log("errorData : ", errorData);
//                 setIsAuthenticated(false); // 실패 시 인증 상태 false로 설정
//                 setErrorMessage(errorData.message || '로그인 실패'); // 에러 메시지 설정
//             }
//         } catch (error) {
//             console.error('로그인 오류:', error);
//             setIsAuthenticated(false); // 오류 발생 시 인증 상태 false로 설정
//             setErrorMessage('로그인에 실패했습니다. 다시 시도해 주세요.'); // 오류 메시지 설정
//         }
//     };
//
//     return (
//         <div className="login-container">
//             <h2>로그인</h2>
//             <form onSubmit={handleSubmit} method="POST">
//                 <div className="form-group">
//                     <label htmlFor="email">이메일</label>
//                     <input
//                         type="email"
//                         id="email"
//                         name="email"
//                         value={email}
//                         onChange={(e) => setEmail(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label htmlFor="password">비밀번호</label>
//                     <input
//                         type="password"
//                         id="password"
//                         name="password"
//                         value={password}
//                         onChange={(e) => setPassword(e.target.value)}
//                         required
//                     />
//                 </div>
//                 {errorMessage && <p className="error-message">{errorMessage}</p>}
//                 <div className="button-group">
//                     <button type="submit" className="button login-button">
//                         로그인
//                     </button>
//                     <button
//                         type="button"
//                         className="button button-cancel login-button"
//                         onClick={() => navigate('/signup')}
//                     >
//                         회원가입
//                     </button>
//                 </div>
//                 <div className="social-login">
//                     <button className="kakao-login">카카오 로그인</button>
//                     <button className="google-login">구글 로그인</button>
//                     <button className="naver-login">네이버 로그인</button>
//                 </div>
//             </form>
//         </div>
//     );
// }
//
// export default Login;
