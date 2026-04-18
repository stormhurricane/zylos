import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/axios';
import { ProfileResponse } from '../api/types';
import { courseApi, Course } from '../api/courseApi';
import { Navbar } from '../components/Navbar';
import { useAuth } from '../context/AuthContext';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Profile = () => {
    const { id } = useParams<{ id: string }>();
    const [profile, setProfile] = useState<ProfileResponse | null>(null);
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);
    const { user } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const endpoint = id ? `/users/${id}` : '/users/me';
                const response = await api.get<ProfileResponse>(endpoint);
                console.log("Geladene Profil-ID:", response.data.id);
                setProfile(response.data);
            } catch (err) {
                console.error("Fehler beim Laden des Profils", err);
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, [id]);

    const currentUserId = user?.userId;

    useEffect(() => {
        const isOwn = !id || String(id) === String(currentUserId);
        if (isOwn) { // Nur Kurse laden, wenn es das eigene Profil ist
            courseApi.getMyCourses()
                .then(res => setCourses(res.data))
                .catch(err => console.error("Fehler beim Laden der Profil-Kurse", err));
        }
    }, [id, currentUserId]);

    if (loading) return <div style={{ padding: '20px' }}>Lädt...</div>;
    if (!profile) return <div style={{ padding: '20px' }}>Profil nicht gefunden.</div>;

    const isOwnProfile = !id || String(id) === String(currentUserId);

    return (
        <div style={{ minHeight: '100vh', backgroundColor: 'var(--color-light)' }}>
            <Navbar />
            <div style={{ padding: '40px', maxWidth: '800px', margin: '0 auto' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '20px', width: '100%' }}>
                    <button onClick={() => navigate('/dashboard')} className="btn-primary" style={{ width: 'auto' }}>
                        ← Zurück
                    </button>
                    {isOwnProfile && (
                        <button onClick={() => navigate('/profile/edit')} className="btn-primary" style={{ width: 'auto', background: 'var(--color-secondary)' }}>
                            Profil bearbeiten
                        </button>
                    )}
                </div>
                
                <div className="auth-card" style={{ maxWidth: '100%' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '30px' }}>
                        <img 
                            src={profile.profilePicture || `${DEFAULT_AVATAR}${profile.firstName}+${profile.lastName}`} 
                            alt="Profile" 
                            style={{ width: '100px', height: '100px', borderRadius: '50%', objectFit: 'cover' }} 
                        />
                        <div>
                            <h1 style={{ margin: 0 }}>{profile.firstName} {profile.lastName}</h1>
                            <p style={{ color: 'var(--text-muted)', margin: '5px 0' }}>{profile.email}</p>
                        </div>
                    </div>

                    <hr style={{ border: '0', borderTop: '1px solid #eee', marginBottom: '30px' }} />

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
                        <div className="info-block">
                            <label style={{ fontWeight: 'bold', color: 'var(--text-muted)' }}>Rolle / Details</label>
                            {profile.matriculationNumber ? (
                                <p>Student (Matrikelnr: {profile.matriculationNumber})</p>
                            ) : (
                                <p>Lehrender</p>
                            )}
                        </div>
                        
                        {profile.matriculationNumber ? (
                            <div className="info-block">
                                <label style={{ fontWeight: 'bold', color: 'var(--text-muted)' }}>Studienfach</label>
                                <p>{profile.studySubject || 'Nicht angegeben'}</p>
                            </div>
                        ) : (
                            <>
                                <div className="info-block">
                                    <label style={{ fontWeight: 'bold', color: 'var(--text-muted)' }}>Lehrstuhl</label>
                                    <p>{profile.chair || 'Nicht angegeben'}</p>
                                </div>

                                <div className="info-block">
                                    <label style={{ fontWeight: 'bold', color: 'var(--text-muted)' }}>Forschungsgebiet</label>
                                    <p>{profile.researchArea || 'Nicht angegeben'}</p>
                                </div>
                            </>
                        )}

                        <div className="info-block">
                            <label style={{ fontWeight: 'bold', color: 'var(--text-muted)' }}>Private Adresse</label>
                            <p>{profile.privateAddress || 'Nicht angegeben'}</p>
                        </div>
                    </div>
                </div>

                {isOwnProfile && (
                    <div className="auth-card" style={{ maxWidth: '100%', marginTop: '30px' }}>
                        <h2 style={{ marginBottom: '20px', color: 'var(--color-primary)' }}>Meine Kurse</h2>
                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '15px' }}>
                            {courses.length > 0 ? courses.map(course => (
                                <div 
                                    key={course.id} 
                                    onClick={() => navigate(`/courses/${course.id}`)}
                                    style={{ 
                                        padding: '15px', 
                                        border: '1px solid #eee', 
                                        borderRadius: '8px', 
                                        cursor: 'pointer',
                                        backgroundColor: '#f9fafb'
                                    }}
                                >
                                    <div style={{ fontWeight: 'bold', fontSize: '0.95rem' }}>{course.title}</div>
                                    <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{course.term} {course.academicYear}</div>
                                </div>
                            )) : (
                                <p style={{ color: 'var(--text-muted)' }}>Du bist noch in keinen Kursen eingeschrieben.</p>
                            )}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};