import {useState} from 'react';
import { AuthContext } from './AuthContext';
import {HTTP_STATUS} from "@/constants/HttpStatusCodes.js";
import {HTTP_METHODS} from "@/constants/HttpMethods.js";
import {buildAuthorizationHeader, parseBearerToken} from "@/utils/TokenUtils.js";

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

            const accessToken = parseBearerToken(response.headers.get('authorization'));
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
                method: HTTP_METHODS.GET,
                headers: {
                    'Accept' : 'application/json',
                    'Authorization': buildAuthorizationHeader(accessToken),
                },
                credentials: "include",
            });

            if(response.status === HTTP_STATUS.UNAUTHORIZED) {
                const errorData = await response.json();
                throw new Error(errorData.message || "리프레시 토큰이 만료되었습니다.");
            }

            const updatedAccessToken = parseBearerToken(response.headers.get('authorization'));
            localStorage.setItem('authorization', updatedAccessToken);

            return updatedAccessToken;
        } catch (error) {
            console.error("Error : ", error);
            localStorage.removeItem('authorization');
            setIsAuthenticated(false);
            setLoading(false);
            setEmail('');
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
