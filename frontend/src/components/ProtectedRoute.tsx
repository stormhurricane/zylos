import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { PageLoader } from './PageLoader';
import { UserRole } from '../api/types'; // Import UserRole from your central types file

interface ProtectedRouteProps {
    children: React.ReactNode;
    requiredRole?: UserRole;
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requiredRole }) => {
    const { user, isInstructor, loading } = useAuth();
    const location = useLocation();

    // 1. Prevents the "redirect flash" while the session is being loaded
    if (loading) {
        return <PageLoader message="Zugriffsberechtigung wird geprüft..." />;
    }

    if (!user) { // If no user is logged in
        // 2. Pass the current location in state to redirect back after login
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    if (requiredRole === 'TEACHER' && !isInstructor) {
        return <Navigate to="/dashboard" replace />;
    }

    return <>{children}</>;
};
