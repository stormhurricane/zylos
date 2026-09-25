import { createBrowserRouter, Navigate, } from 'react-router-dom';
import { Login } from './auth/components/Login';
import { Register } from './auth/components/Register';

const router = createBrowserRouter([
    { 
        path: "/",
        children: [
            { index: true, element: <Navigate to="login" replace /> },
            { path: "login", element: <Login /> },
            { path: "register", element: <Register /> }
        ]
    }
]);

export default router;