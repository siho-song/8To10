import { useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/context/UseAuth.jsx';

import "@/styles/login/Login.css";
import publicApi from "@/api/PublicApi.js";
import {parseBearerToken} from "@/helpers/TokenUtils.js";
import PublicHeader from "@/components/PublicHeader.jsx";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

function Login() {

    const [inputEmail, setInputEmail] = useState('');
    const [password, setPassword] = useState('');
    const { errorMessage } = useAuth();

    const {isAuthenticated, setIsAuthenticated, setLoading, setEmail } = useAuth();

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const url = '/login'
            const response = await publicApi.post(
                url,
                {
                email:inputEmail,
                password:password,
            },{
                apiEndPoint: API_ENDPOINT_NAMES.LOGIN,
            });

            const accessToken = response.headers['authorization'];
            localStorage.setItem('Authorization', parseBearerToken(accessToken));
            localStorage.setItem('Email', inputEmail);
            setIsAuthenticated(true);
            setEmail(inputEmail);
        } catch (error) {
            console.error(error.toString());
            console.error(error);
            setIsAuthenticated(false);
        } finally {
            setLoading(false);
        }
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
                        value={inputEmail}
                        onChange={(e) => setInputEmail(e.target.value)}
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
