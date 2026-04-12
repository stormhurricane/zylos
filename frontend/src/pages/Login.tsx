import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { LandingLayout } from '../components/LandingLayout';

export const Login = () => {
    const [identifier, setIdentifier] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState<{identifier?: string, password?: string}>({});
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setFieldErrors({});

        // Manuelle Validierung statt 'required'
        const errors: {identifier?: string, password?: string} = {};
        if (!identifier) errors.identifier = 'Bitte gib deine E-Mail oder Matrikelnummer ein.';
        if (!password) errors.password = 'Bitte gib dein Passwort ein.';

        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors);
            return;
        }

        try {
            await login({ identifier, password });
            navigate('/dashboard');
        } catch (err: any) {
            setError(err.response?.data?.error || 'Login fehlgeschlagen. Bitte Daten prüfen.');
        }
    };

    return (
        <LandingLayout>
        <div className="auth-card">
            <h2 style={{ marginBottom: '10px', fontSize: '1.8rem' }}>Willkommen zurück</h2>
            <p style={{ color: 'var(--text-muted)', marginBottom: '30px' }}>Bitte logge dich in dein Konto ein.</p>
            
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label className="required">E-Mail oder Matrikelnr.</label>
                    <input 
                        type="text" 
                        className="form-input"
                        placeholder="z.B. 1000001"
                        value={identifier} 
                        onChange={(e) => setIdentifier(e.target.value)}
                    />
                    {fieldErrors.identifier && <div className="error-message">{fieldErrors.identifier}</div>}
                </div>
                <div className="form-group">
                    <label className="required">Passwort</label>
                    <input 
                        type="password" 
                        className="form-input"
                        placeholder="••••••••"
                        value={password} 
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    {fieldErrors.password && <div className="error-message">{fieldErrors.password}</div>}
                </div>
                {error && <p style={{ color: 'red' }}>{error}</p>}
                <button type="submit" className="btn-primary">
                    Anmelden
                </button>
            </form>
            <p style={{ marginTop: '25px', textAlign: 'center', fontSize: '0.9rem', color: 'var(--text-muted)' }}>
                Noch kein Konto? <span style={{ color: 'var(--color-secondary)', fontWeight: '600', cursor: 'pointer' }} onClick={() => navigate('/register')}>Jetzt registrieren</span>
            </p>
        </div>
        </LandingLayout>
    );
};
