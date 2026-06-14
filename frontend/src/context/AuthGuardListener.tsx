import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const AuthGuardListener = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const handleUnauthorized = () => {
            navigate('/login', { state: { error: 'Session abgelaufen. Bitte neu anmelden.' } });
        };

        window.addEventListener('auth-unauthorized', handleUnauthorized);
        return () => window.removeEventListener('auth-unauthorized', handleUnauthorized);
    }, [navigate]);

    return null; 
};