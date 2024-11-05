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
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                credentials: 'include',
                body: paramString.toString(),
            }));
            console.log("response : ", response.ok);
            if (response.ok) {
                setIsAuthenticated(true);
                setErrorMessage('');
                setEmail(email);
            } else {
                const errorData = await response.json();
                setIsAuthenticated(false);
                setErrorMessage(errorData.message || '로그인 실패');
            }
        } catch (error) {
            console.error("Error : ", error);
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
