import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "@/context/auth/UseAuth.jsx";
import {useEffect} from "react";
import {refreshAccessToken} from "@/helpers/TokenManager.js";
import PrivateHeader from "@/components/PrivateHeader.jsx";

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
                    }
                } catch(error) {
                    logout(() =>{
                        console.log("로그아웃");
                    });
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
