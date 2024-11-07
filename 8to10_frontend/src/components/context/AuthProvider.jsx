import {useState} from 'react';
import { AuthContext } from './AuthContext';

function AuthProvider({ children }) {

    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [email, setEmail] = useState('');

    const loginUser = async (email, password) => {

        const paramString = new URLSearchParams({email, password});
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
                setErrorMessage(errorData.message + ' : 로그인에 실패했습니다.');
                throw new Error(errorMessage);
            }

            const accessToken = response.headers.get('authorization').split(' ')[1];
            if (accessToken) {
                localStorage.setItem('authorization', accessToken);
                setIsAuthenticated(true);
                setEmail(email);
            } else {
                throw new Error("액세스 토큰을 확인할 수 없습니다.");
            }
        } catch (error) {
            console.error(error);
            setIsAuthenticated(false);
        } finally {
            setLoading(false);
        }
    };

    const refreshAccessToken = async () => {
        try {
            const accessToken = localStorage.getItem('authorization');
            const response = await fetch('/api/renew', {
                method: "GET",
                headers: {
                    'authorization': `Bearer ${accessToken}`,
                },
                credentials: "include",
            });

            if (!response.ok) {
                if(response.status === 401) {
                    localStorage.removeItem('authorization');
                    setIsAuthenticated(false);
                    setLoading(false);
                    setEmail('');
                }
                throw new Error("액세스 토큰을 갱신 할 수 없습니다.");
            }
            const newAccessToken = response.headers.get('authorization')?.split(' ')[1];
            if (!newAccessToken){
                throw new Error("갱신한 액세스 토큰을 확인할 수 없습니다.")
            }
            localStorage.setItem('authorization', newAccessToken);
            return newAccessToken;
        } catch (error) {
            console.error("Error : ", error);
            return null;
        }
    }

    return (
        <AuthContext.Provider value={{ isAuthenticated, setIsAuthenticated, refreshAccessToken, loading, setLoading, loginUser, email, errorMessage }}>
            {children}
        </AuthContext.Provider>
    );
}


export default AuthProvider;
