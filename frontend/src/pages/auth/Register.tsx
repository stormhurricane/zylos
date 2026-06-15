import React from 'react';
import { Link } from 'react-router-dom';
import { LandingLayout } from '../../components/LandingLayout';
import styles from './Register.module.css';
import { useRegister } from './useRegister';

export const Register: React.FC = () => {
    const {
        userType,
        setUserType,
        error,
        fieldErrors,
        success,
        isSubmitting,
        formData,
        handleChange,
        handleFileChange,
        handleSubmit 
    } = useRegister(); 

    if (success) {
        return (
            <LandingLayout>
                <div className={`card auth-card ${styles.successCard}`}>
                    <h2 className={styles.successTitle}>Registrierung erfolgreich!</h2>
                    <p>Du wirst in Kürze zum Login weitergeleitet...</p>
                </div>
            </LandingLayout>
        );
    }

    return (
        <LandingLayout>
            <div className="card auth-card">
                <h2 className={styles.headerTitle}>Konto erstellen</h2>
                <p className={styles.headerSubtitle}>Werde Teil der Zylos-Community.</p>

                {/* User type toggle */}
                <div className={styles.toggleContainer}>
                    <button 
                        type="button"
                        onClick={() => setUserType('student')}
                        className={`btn-primary ${styles.toggleButton} ${
                            userType === 'student' ? styles.toggleButtonActive : ''
                        }`}
                    >
                        Student
                    </button>
                    <button 
                        type="button"
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
                            <label htmlFor="firstName" className="required">Vorname</label>
                            <input id="firstName" name="firstName" className="form-input" placeholder="Max" onChange={handleChange} disabled={isSubmitting} />
                            {fieldErrors.firstName && <div className="error-message">{fieldErrors.firstName}</div>}
                        </div>
                        <div className={`form-group ${styles.flex1}`}>
                            <label htmlFor="lastName" className="required">Nachname</label>
                            <input id="lastName" name="lastName" className="form-input" placeholder="Mustermann" onChange={handleChange} disabled={isSubmitting} />
                            {fieldErrors.lastName && <div className="error-message">{fieldErrors.lastName}</div>}
                        </div>
                    </div>

                    <div className="form-group">
                        <label htmlFor="email" className="required">E-Mail Adresse</label>
                        <input 
                            id="email"
                            name="email" 
                            type="email" 
                            className="form-input" 
                            placeholder="max.mustermann@stud.uni.de" onChange={handleChange} disabled={isSubmitting} />
                        {fieldErrors.email && <div className="error-message">{fieldErrors.email}</div>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="password" className="required">Passwort</label>
                        <input id="password" name="password" type="password" className="form-input" placeholder="Mind. 8 Zeichen" onChange={handleChange} disabled={isSubmitting} />
                        {fieldErrors.password && <div className="error-message">{fieldErrors.password}</div>}
                    </div>

                    <div className="form-group">
                        <label htmlFor="privateAddress">Private Adresse</label>
                        <input id="privateAddress" name="privateAddress" className="form-input" placeholder="Musterstraße 1, 12345 Stadt" onChange={handleChange} disabled={isSubmitting} />
                    </div>

                    <div className="form-group">
                        <label htmlFor="profilePicture">Profilbild</label>
                        <input id="profilePicture" type="file" accept="image/*" className="form-input" onChange={handleFileChange} disabled={isSubmitting} />
                        {formData.profilePicture && (
                            <img 
                                src={formData.profilePicture} 
                                alt="Preview" 
                                className={`avatar-img ${styles.previewImage}`} 
                                style={{ width: '50px', height: '50px', marginTop: 'var(--spacing-sm)' }} 
                            />
                        )}
                    </div>

                    {/* Dynamic fields depending on user type */}
                    {userType === 'student' ? (
                        <div className="form-group">
                            <label htmlFor="studySubject">Studienfach</label>
                            <input id="studySubject" name="studySubject" className="form-input" placeholder="z.B. Informatik" onChange={handleChange} disabled={isSubmitting} />
                        </div>
                    ) : (
                        <>
                            <div className="form-group">
                                <label htmlFor="chair">Lehrstuhl</label>
                                <input id="chair" name="chair" className="form-input" placeholder="z.B. Software Engineering" onChange={handleChange} disabled={isSubmitting} />
                            </div>
                            <div className="form-group">
                                <label htmlFor="researchArea">Forschungsgebiet</label>
                                <input id="researchArea" name="researchArea" className="form-input" placeholder="z.B. KI & Ethik" onChange={handleChange} disabled={isSubmitting} />
                            </div>
                        </>
                    )}

                    {error && <p className="error-message">{error}</p>}

                    <button 
                        type="submit" 
                        className={`btn-primary btn-block ${styles.submitButton}`}
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Wird registriert...' : 'Jetzt registrieren'}
                    </button>
                </form>

                <p className={styles.footer}>
                    Bereits registriert?{' '}
                    <Link to="/login" className={styles.link}>
                        Zum Login
                    </Link>
                </p>
            </div>
        </LandingLayout>
    );
};