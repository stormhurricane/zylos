import { createBrowserRouter, Navigate, } from 'react-router-dom';
import { Login } from './auth/components/Login';
import { Register } from './auth/components/Register';
import { ProtectedRoute } from './auth/guards/ProtectedRoute';
import { Dashboard } from './features/dashboard/pages/Dashboard';
import { AuthLayout } from './layouts/AuthLayout';
import { AppLayout } from './layouts/AppLayout';

const router = createBrowserRouter([
    { 
        path: "/",
        children: [
            { index: true, element: <Navigate to="login" replace /> },
            {
                element: <AuthLayout/>,
                children: [
                    { path: "login", element: <Login /> },
                    { path: "register", element: <Register /> },
                ]
            },

            {
                element: <ProtectedRoute/>,
                children: [
                    {
                        element: <AppLayout/>,
                        children: [
                            { path: "home", element: <Dashboard/>}
                        ]
                    },
                ]
            }
        ]
    }
]);

export default router;