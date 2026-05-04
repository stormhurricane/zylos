import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { LandingLayout } from '../components/LandingLayout';
import api from '../api/axios';
import styles from './Register.module.css';

export const Register = () => {
    const navigate = useNavigate();
    const [userType, setUserType] = useState<'student' | 'teacher'>('student');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);

    // Formular-State
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        privateAddress: '',
        studySubject: '', // Nur Student
        chair: '',        // Nur Lehrer
        researchArea: '', // Nur Lehrer
        profilePicture: ''
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setFormData({ ...formData, profilePicture: reader.result as string });
            };
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        
        try {
            const endpoint = userType === 'student' ? '/users/register/student' : '/users/register/teacher';
            await api.post(endpoint, formData);
            setSuccess(true);
            setTimeout(() => navigate('/login'), 3000);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Registrierung fehlgeschlagen.');
        }
    };

    if (success) {
        return (
            <LandingLayout>
                <div className={`auth-card ${styles.successCard}`}>
                    <h2 className={styles.successTitle}>Registrierung erfolgreich!</h2>
                    <p>Du wirst in Kürze zum Login weitergeleitet...</p>
                </div>
            </LandingLayout>
        );
    }

    return (
        <LandingLayout>
            <div className="auth-card">
                <h2 className={styles.headerTitle}>Konto erstellen</h2>
                <p className={styles.headerSubtitle}>Werde Teil der Zylos-Community.</p>

                {/* Toggle für Nutzertyp */}
                <div className={styles.toggleContainer}>
                    <button 
                        onClick={() => setUserType('student')}
                        className={`btn-primary ${styles.toggleButton} ${
                            userType === 'student' ? styles.toggleButtonActive : ''
                        }`}
                    >
                        Student
                    </button>
                    <button 
                        onClick={() => setUserType('teacher')}
                        className={`btn-primary ${styles.toggleButton} ${
                            userType === 'teacher' ? styles.toggleButtonActive : ''
                        }`}
                    >
                        Lehrender
                    </button>
                </div>

                <form onSubmit={handleSubmit}>
                    <div className={styles.row}>
                        <div className={`form-group ${styles.flex1}`}>
                            <label className="required">Vorname</label>
                            <input name="firstName" className="form-input" placeholder="Max" required onChange={handleChange} />
                        </div>
                        <div className={`form-group ${styles.flex1}`}>
                            <label className="required">Nachname</label>
                            <input name="lastName" className="form-input" placeholder="Mustermann" required onChange={handleChange} />
                        </div>
                    </div>

                    <div className="form-group">
                        <label className="required">E-Mail Adresse</label>
                        <input 
                            name="email" 
                            type="email" 
                            className="form-input" 
                            placeholder="max.mustermann@stud.uni.de" required onChange={handleChange} />
                    </div>

                    <div className="form-group">
                        <label className="required">Passwort</label>
                        <input name="password" type="password" className="form-input" placeholder="Mind. 8 Zeichen" required onChange={handleChange} />
                    </div>

                    <div className="form-group">
                        <label>Private Adresse</label>
                        <input name="privateAddress" className="form-input" placeholder="Musterstraße 1, 12345 Stadt" onChange={handleChange} />
                    </div>

                    <div className="form-group">
                        <label>Profilbild</label>
                        <input type="file" accept="image/*" className="form-input" onChange={handleFileChange} />
                        {formData.profilePicture && (
                            <img 
                                src={formData.profilePicture} 
                                alt="Preview" className={styles.previewImage} />
                        )}
                    </div>

                    {/* Dynamische Felder je nach Typ */}
                    {userType === 'student' ? (
                        <div className="form-group">
                            <label>Studienfach</label>
                            <input name="studySubject" className="form-input" placeholder="z.B. Informatik" onChange={handleChange} />
                        </div>
                    ) : (
                        <>
                            <div className="form-group">
                                <label>Lehrstuhl</label>
                                <input name="chair" className="form-input" placeholder="z.B. Software Engineering" onChange={handleChange} />
                            </div>
                            <div className="form-group">
                                <label>Forschungsgebiet</label>
                                <input name="researchArea" className="form-input" placeholder="z.B. KI & Ethik" onChange={handleChange} />
                            </div>
                        </>
                    )}

                    {error && <p className={styles.errorMessage}>{error}</p>}

                    <button type="submit" className={`btn-primary ${styles.submitButton}`}>
                        Jetzt registrieren
                    </button>
                </form>

                <p className={styles.footer}>
                    Bereits registriert?{' '}
                    <span 
                        onClick={() => navigate('/login')}
                        className={styles.link}
                    >
                        Zum Login
                    </span>
                </p>
            </div>
        </LandingLayout>
    );
};
