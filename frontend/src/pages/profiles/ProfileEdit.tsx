import { PageLoader } from '../../components/PageLoader';
import styles from './ProfileEdit.module.css';
import { useProfileEdit } from './useProfileEdit';

export const ProfileEdit = () => {
    const {
        
        formData,
        displayName,
        isStudent,
        loading,
        isSubmitting,
        error,
        handleChange,
        handleFileChange,
        handleSubmit,
        cancel
    } = useProfileEdit();

    if (loading) return <PageLoader message="Einstellungen werden geladen..." />;

    return (
        <div className="app-page">
            <div className="container container-narrow text-center" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <div className="card" style={{ textAlign: 'left' }}>
                    <h2>Profil bearbeiten</h2>
                    <p className={styles.subtitle}>
                        Ändere deine persönlichen Informationen für {displayName.firstName} {displayName.lastName}.
                    </p>

                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>Profilbild</label>
                            <input type="file" accept="image/*" className="form-input" onChange={handleFileChange} />
                            {formData.profilePicture && (
                                <img src={formData.profilePicture} alt="Preview" className="avatar-img" style={{ width: '80px', height: '80px', marginTop: 'var(--spacing-sm)' }} />
                            )}
                        </div>

                        <div className="form-group">
                            <label>Neues Passwort (leer lassen für keine Änderung)</label>
                            <input 
                                name="password" 
                                type="password" 
                                className="form-input" 
                                placeholder="••••••••" 
                                onChange={handleChange} 
                            />
                        </div>

                        <div className="form-group">
                            <label>Private Adresse</label>
                            <input 
                                name="privateAddress" 
                                className="form-input" 
                                value={formData.privateAddress || ''} 
                                onChange={handleChange} 
                            />
                        </div>

                        {isStudent ? (
                            <div className="form-group">
                                <label>Studienfach</label>
                                <input 
                                    name="studySubject" 
                                    className="form-input" 
                                    value={formData.studySubject || ''} 
                                    onChange={handleChange} 
                                />
                            </div>
                        ) : (
                            <>
                                <div className="form-group">
                                    <label>Lehrstuhl</label>
                                    <input name="chair" className="form-input" value={formData.chair || ''} onChange={handleChange} />
                                </div>
                                <div className="form-group">
                                    <label>Forschungsgebiet</label>
                                    <input name="researchArea" className="form-input" value={formData.researchArea || ''} onChange={handleChange} />
                                </div>
                            </>
                        )}

                        {error && <p className="error-message">{error}</p>}

                        <div className={styles.buttonGroup}>
                            <button 
                                type="submit" 
                                className="btn-primary" 
                                disabled={isSubmitting}
                            >
                                {isSubmitting ? 'Speichern...' : 'Änderungen speichern'}
                            </button>
                            <button type="button" className="btn-secondary" onClick={cancel}>Abbrechen</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};