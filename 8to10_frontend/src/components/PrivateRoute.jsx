import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "@/context/UseAuth.jsx";
import {useEffect} from "react";
import {refreshAccessToken} from "@/helpers/TokenManager.js";

function PrivateRoute() {
    const {isAuthenticated, setIsAuthenticated, loading, setLoading, setEmail} = useAuth();

    useEffect(() => {
        const refreshAuthentication = async () => {
            if (!isAuthenticated) {
                try {
                    const accessToken = localStorage.getItem('Authorization');
                    const email = localStorage.getItem('Email');

                    if (accessToken && email) {
                        const response = await refreshAccessToken();
                        if (response) {
                            setIsAuthenticated(true);
                            setEmail(email);
                        }
                    }
                } catch(error) {
                    console.error(error.toString());
                    console.error(error);
                } finally {
                    setLoading(false);
                }
            }
        }
        if (!isAuthenticated) {
            setLoading(true);
        }
        refreshAuthentication();
    }, [isAuthenticated]);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    return isAuthenticated ? <Outlet /> : <Navigate to="/" />;
}

export default PrivateRoute;
