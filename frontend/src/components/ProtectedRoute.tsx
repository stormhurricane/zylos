import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface ProtectedRouteProps {
    children: React.ReactNode;
    requiredRole?: 'STUDENT' | 'INSTRUCTOR';

}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRole }) => {
    const { user } = useAuth();

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    if (requiredRole === 'INSTRUCTOR' && ('matriculationNumber' in user)) {
        return <Navigate to="/dashboard" replace />;
    }

    return <>{children}</>;
};
