import { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate, useLocation } from "react-router-dom";

export const useLogin = () => {
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState<{ identifier?: string; password?: string }>({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    
    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    // Typ-Sicherheit für das Event garantieren ohne kompletten React-Import
    const handleSubmit = async (e: { preventDefault: () => void }) => {
        e.preventDefault();
        setError('');
        setFieldErrors({});

        const errors: { identifier?: string; password?: string } = {};
        if (!identifier.trim()) errors.identifier = 'Bitte gib deine E-Mail oder Matrikelnummer ein.';
        if (!password) errors.password = 'Bitte gib dein Passwort ein.';

        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors);
            return;
        }

        setIsSubmitting(true);
        try {
            await login({ identifier, password });
            // Das Routing gehört genau hierhin, da es Teil der Business-Logik nach Erfolg ist
            const origin = (location.state as any)?.from?.pathname || '/dashboard';
            navigate(origin);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Login fehlgeschlagen. Bitte Daten prüfen.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return {
        identifier,
        password,
        error,
        fieldErrors,
        isSubmitting,
        setIdentifier,
        setPassword,
        handleSubmit
    };
};