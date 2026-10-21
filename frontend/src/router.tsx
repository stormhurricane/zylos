import { createBrowserRouter, Navigate, } from 'react-router-dom';
import { Login } from './auth/components/Login';
import { Register } from './auth/components/Register';
import { ProtectedRoute } from './auth/guards/ProtectedRoute';
import { Dashboard } from './pages/Dashboard';

const router = createBrowserRouter([
    { 
        path: "/",
        children: [
            { index: true, element: <Navigate to="login" replace /> },
            { path: "login", element: <Login /> },
            { path: "register", element: <Register /> },

            {
                element: <ProtectedRoute/>,
                children: [
                    { path: "dashboard", element: <Dashboard/>}
                ]
            }
        ]
    }
]);

export default router;