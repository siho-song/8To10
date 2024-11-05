import { Navigate, Outlet } from 'react-router-dom';
import {useAuth} from "@/components/context/UseAuth.jsx";

function PrivateRoute() {
    const {isAuthenticated, loading} = useAuth();

    console.log("isAuthenticated : ", isAuthenticated);
    console.log("loading : ", loading);

    if (loading) {
        return <div>로딩 중...</div>;
    }

    return isAuthenticated ? <Outlet /> : <Navigate to="/" />;
}

export default PrivateRoute;
