import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "@/context/UseAuth.jsx";
import {useEffect} from "react";

function PrivateRoute() {
    const {isAuthenticated, setIsAuthenticated, loading, setLoading, setEmail, errorMessage} = useAuth();

    useEffect(() => {
        if (!isAuthenticated && !errorMessage) {
            const accessToken = localStorage.getItem('Authorization');
            const email = localStorage.getItem('Email');

            if (accessToken && email) {
                setIsAuthenticated(true);
                setEmail(email);
            } else {
                setIsAuthenticated(false);
                localStorage.clear();
            }
            setLoading(false);
        }
    }, [isAuthenticated]);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    return isAuthenticated ? <Outlet /> : <Navigate to="/" />;
}

export default PrivateRoute;
