import { useNavigate, useLocation } from 'react-router-dom';
import { LandingLayout } from '../../components/LandingLayout';
import styles from './Register.module.css'; // Sharing auth styles with Register component
import { useLogin } from './useLogin';

export const Login = () => {
    const {       
        identifier,
        password,
        error,
        fieldErrors,
        isSubmitting,
        setIdentifier,
        setPassword,
        handleSubmit
    } = useLogin();
    const navigate = useNavigate();

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
