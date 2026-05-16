import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { userApi } from '../../api/userApi';
import { ProfileResponse, ProfileUpdateRequest } from '../../api/types';
import { PageLoader } from '../../components/PageLoader';
import styles from './ProfileEdit.module.css';

export const ProfileEdit = () => {
    const [formData, setFormData] = useState<ProfileUpdateRequest & { 
        firstName?: string, 
        lastName?: string,
        chair?: string,
        researchArea?: string,
        studySubject?: string
    }>({
        password: '',
        privateAddress: '',
        profilePicture: '',
        chair: '',
        researchArea: '',
        studySubject: '',
        firstName: '',
        lastName: ''
    });
    const [isStudent, setIsStudent] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await userApi.getProfile();
                const data = response.data;
                setFormData({
                    password: '',
                    privateAddress: data.privateAddress || '',
                    profilePicture: data.profilePicture || '',
                    studySubject: data.studySubject || '',
                    chair: data.chair || '',
                    researchArea: data.researchArea || '',
                    firstName: data.firstName, // Nur zur Anzeige
                    lastName: data.lastName    // Nur zur Anzeige
                });
                setIsStudent(!!data.matriculationNumber);
            } catch (err) {
                setError('Profil konnte nicht geladen werden.');
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, []);

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
        try {
            // Filter out display-only fields before sending to API
            const { firstName, lastName, ...updateData } = formData;
            await userApi.updateProfile(updateData);
            navigate('/profile');
        } catch (err) {
            setError('Update fehlgeschlagen.');
        }
    };

    if (loading) return <PageLoader message="Einstellungen werden geladen..." />;

    return (
        <div className="app-page">
            <div className="container container-narrow text-center" style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <div className="card" style={{ textAlign: 'left' }}>
                    <h2>Profil bearbeiten</h2>
                    <p className={styles.subtitle}>
                        Ändere deine persönlichen Informationen für {formData.firstName} {formData.lastName}.
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
                            <button type="submit" className="btn-primary">Speichern</button>
                            <button type="button" className={`btn-primary ${styles.cancelBtn}`} onClick={() => navigate('/profile')}>Abbrechen</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};