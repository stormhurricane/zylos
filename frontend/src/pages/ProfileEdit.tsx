import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { ProfileResponse, ProfileUpdateRequest } from '../api/types';
import { Navbar } from '../components/Navbar';

export const ProfileEdit = () => {
    const [formData, setFormData] = useState<ProfileUpdateRequest & { firstName?: string, lastName?: string }>({
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
                const response = await api.get<ProfileResponse>('/users/me');
                const data = response.data;
                setFormData({
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
            await api.put('/users/me', updateData);
            navigate('/profile');
        } catch (err) {
            setError('Update fehlgeschlagen.');
        }
    };

    if (loading) return <div style={{ padding: '20px' }}>Lädt...</div>;

    return (
        <div style={{ minHeight: '100vh', backgroundColor: 'var(--color-light)' }}>
            <Navbar />
            <div style={{ padding: '40px', maxWidth: '600px', margin: '0 auto' }}>
                <div className="auth-card">
                    <h2>Profil bearbeiten</h2>
                    <p style={{ color: 'var(--text-muted)', marginBottom: '25px' }}>
                        Ändere deine persönlichen Informationen für {formData.firstName} {formData.lastName}.
                    </p>

                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label>Profilbild</label>
                            <input type="file" accept="image/*" className="form-input" onChange={handleFileChange} />
                            {formData.profilePicture && (
                                <img src={formData.profilePicture} alt="Preview" style={{ width: '80px', height: '80px', borderRadius: '50%', marginTop: '10px', objectFit: 'cover' }} />
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

                        {error && <p style={{ color: 'red', marginBottom: '15px' }}>{error}</p>}

                        <div style={{ display: 'flex', gap: '10px' }}>
                            <button type="submit" className="btn-primary">Speichern</button>
                            <button type="button" className="btn-primary" style={{ background: '#e2e8f0', color: 'var(--text-main)' }} onClick={() => navigate('/profile')}>Abbrechen</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
};