import {useState} from 'react';
import { AuthContext } from './AuthContext';

function AuthProvider({ children }) {

    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [email, setEmail] = useState('');

    const loginUser = async (email, password) => {

        const paramString = new URLSearchParams({email, password});
        console.log("loginUser 실행")
        try {
            const response = (await fetch(`/api/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: paramString.toString(),
            }));

            if (!response.ok) {
                const errorData = await response.json();
                setIsAuthenticated(false);
                setErrorMessage(errorData.message + ' => 로그인에 실패했습니다.');
                throw new Error(errorMessage);
            }

            const accessToken = response.headers.get('authorization').split(' ')[1];
            console.log(accessToken);
            if (accessToken) {
                localStorage.setItem('authorization', accessToken);
                setIsAuthenticated(true);
                setErrorMessage('');
                setEmail(email);
            } else {
                throw new Error("액세스 토큰을 확인할 수 없습니다.");
                setIsAuthenticated(false);
                setErrorMessage(errorData.message || '로그인 실패');
            }
        } catch (error) {
            console.error(error);
            setIsAuthenticated(false);
            setErrorMessage( '로그인 실패');
        } finally {
            setLoading(false);
        }
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, loading, loginUser, email, errorMessage }}>
            {children}
        </AuthContext.Provider>
    );
}


export default AuthProvider;
