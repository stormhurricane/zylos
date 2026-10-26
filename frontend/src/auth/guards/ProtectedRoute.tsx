import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import { LoadingSpinner } from "../../components/ui/LoadingSpinner";

interface ProtectedRouteProps {
    allowedRoles?: string[];
}

export const ProtectedRoute = ({ allowedRoles } : ProtectedRouteProps) => {

    const { user, isLoading } = useAuth();
    const location = useLocation();

    if (isLoading) {
        return <LoadingSpinner label="Authentication is being verified..." />;
    }

    if(!user){
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    if (allowedRoles && !user.roles?.some((role) => allowedRoles.includes(role))){
        return <Navigate to="/unauthorized" replace />;
    }

    return <Outlet/>;
};