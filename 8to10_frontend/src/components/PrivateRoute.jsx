import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "@/components/context/UseAuth.jsx";
import {useEffect} from "react";

function PrivateRoute() {
    const {isAuthenticated, setIsAuthenticated, loading, setLoading} = useAuth();

    useEffect(() => {
        if (!isAuthenticated) {
            const accessToken = localStorage.getItem('authorization');

            if (accessToken) {
                setIsAuthenticated(true);
            } else {
                setIsAuthenticated(false);
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
