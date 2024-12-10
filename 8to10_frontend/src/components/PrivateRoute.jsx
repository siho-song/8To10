import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "@/context/auth/UseAuth.jsx";
import {useEffect} from "react";
import {refreshAccessToken} from "@/helpers/TokenManager.js";
import PrivateHeader from "@/components/PrivateHeader.jsx";
import customErrors from "@/api/CustomErrors.js";

function PrivateRoute() {
    const {isAuthenticated, setIsAuthenticated, loading, setLoading, setEmail, logout} = useAuth();

    useEffect(() => {
        const refreshAuthentication = async () => {
            if (!isAuthenticated) {
                try {
                    const accessToken = localStorage.getItem('Authorization');
                    const email = localStorage.getItem('Email');

                    if (accessToken && email) {
                        const updatedAccessToken = await refreshAccessToken();
                        if (updatedAccessToken) {
                            localStorage.setItem('Authorization', updatedAccessToken);
                            setIsAuthenticated(true);
                            setEmail(email);
                        }
                    } else {
                        throw customErrors(0, 0, "토큰이 없습니다.");
                    }
                } catch(error) {
                    logout();
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

    return isAuthenticated ? (
        <>
            <PrivateHeader />
            <Outlet />
        </>
    ) : (
            <Navigate to="/" />
    );
}

export default PrivateRoute;
