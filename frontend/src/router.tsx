import { createBrowserRouter, Navigate, } from 'react-router-dom';
import { Login } from './features/auth/components/Login';

const router = createBrowserRouter([
    { 
        path: "/",
        children: [
            { index: true, element: <Navigate to="login" replace /> },
            { path: "login", element: <Login /> },         
        ]
    }
]);

export default router;