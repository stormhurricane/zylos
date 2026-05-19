import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';
import { LandingLayout } from '../../components/LandingLayout';
import styles from './Register.module.css'; // Sharing auth styles with Register component

export const Login = () => {
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState<{identifier?: string, password?: string}>({});
    const [isSubmitting, setIsSubmitting] = useState(false);
    
    const { login } = useAuth();
    const location = useLocation(); // Get the current location object
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setFieldErrors({});

        // Manual validation instead of HTML 'required' attribute for better UI control
        const errors: {identifier?: string, password?: string} = {};
        if (!identifier) errors.identifier = 'Bitte gib deine E-Mail oder Matrikelnummer ein.';
        if (!password) errors.password = 'Bitte gib dein Passwort ein.';

        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors);
            return;
        }

        setIsSubmitting(true);
        try {
            await login({ identifier, password });
            // Redirect to the 'from' path if it exists, otherwise to '/dashboard'
            navigate(location.state?.from?.pathname || '/dashboard');
        } catch (err: any) {
            setError(err.response?.data?.error || 'Login fehlgeschlagen. Bitte Daten prüfen.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <LandingLayout>
        <div className="card auth-card">
            <h2 className={styles.headerTitle}>Willkommen zurück</h2>
            <p className={styles.headerSubtitle}>Bitte logge dich in dein Konto ein.</p>
            
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="identifier" className="required">E-Mail oder Matrikelnr..</label>
                    <input
                        id="identifier"
                        type="text" 
                        className="form-input"
                        placeholder="z.B. 1000001"
                        value={identifier} 
                        onChange={(e) => setIdentifier(e.target.value)}
                        disabled={isSubmitting}
                    />
                    {fieldErrors.identifier && <div className="error-message">{fieldErrors.identifier}</div>}
                </div>
                <div className="form-group">
                    <label htmlFor="password" className="required">Passwort</label>
                    <input
                        id="password"
                        type="password" 
                        className="form-input"
                        placeholder="••••••••"
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)} // Fixed duplicate onChange
                        disabled={isSubmitting}
                    />
                    {fieldErrors.password && <div className="error-message">{fieldErrors.password}</div>}
                </div>
                {error && <p className="error-message">{error}</p>}
                <button 
                    type="submit" 
                    className={`btn-primary btn-block ${styles.submitButton}`} // Removed inline style
                    disabled={isSubmitting}
                >
                    {isSubmitting ? 'Wird angemeldet...' : 'Anmelden'}
                </button>
            </form>
            <p className={styles.footer}>
                Noch kein Konto? <span className={styles.link} onClick={() => navigate('/register')}>Jetzt registrieren</span>
            </p>
        </div>
        </LandingLayout>
    );
};
