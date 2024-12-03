import {useEffect, useState} from 'react';
import { AuthContext } from './AuthContext.jsx';
import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";
import {setLogoutHandler} from "@/helpers/AxiosHelper.js";

function AuthProvider({ children }) {

    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [loading, setLoading] = useState(true);
    const [email, setEmail] = useState('');

    const logout = async (onLogout) => {
        try {
            const url = "/logout";
            const response = await authenticatedApi.get(
                url,
                {apiEndPoint: API_ENDPOINT_NAMES.LOGOUT}
            );

            console.log(response);

        } catch (error) {
            console.error(error.toString());
            console.error(error);
        } finally {
            setIsAuthenticated(false);
            setEmail('');
            localStorage.clear();
            if (onLogout) onLogout();
        }
    }

    useEffect(() => {
        setLogoutHandler(logout);
    }, [])

    return (
        <AuthContext.Provider value={
            { isAuthenticated,
                setIsAuthenticated,
                loading, setLoading,
                email,
                setEmail,
                logout,
            }
        }>
            {children}
        </AuthContext.Provider>
    );
}


export default AuthProvider;
