import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { userApi } from '../../api/userApi';
import { ProfileResponse, Course } from '../../api/types';
import axios from 'axios';
import { courseApi } from '../../api/courseApi';
import { PageLoader } from '../../components/PageLoader';
import { useAuth } from '../../context/AuthContext';
import styles from './Profile.module.css';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Profile = () => {
    const { id } = useParams<{ id: string }>();
    const [profile, setProfile] = useState<ProfileResponse | null>(null);
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { user } = useAuth();
    const navigate = useNavigate();

    // 1. Berechne die Variable weiterhin synchron
const currentUserId = user?.userId;
const isOwnProfile = !id || (currentUserId !== undefined && String(id) === String(currentUserId));

useEffect(() => {
    const fetchData = async () => {
        try {
            setLoading(true);
            setError(null);
            
            const response = await userApi.getProfile(id);
            setProfile(response.data);

            // Nutze die Variable einfach hier drinnen, sie muss nicht im Dependency-Array stehen!
            if (isOwnProfile) {
                const coursesRes = await courseApi.getMyCourses();
                setCourses(coursesRes.data);
            }
        } catch (err) {
            console.error("Fehler beim Laden des Profils", err);
            if (axios.isAxiosError(err) && err.response?.status === 404) {
                setProfile(null);
            } else {
                setError("Das Profil konnte nicht geladen werden.");
            }
        } finally {
            setLoading(false);
        }
    };
    
    fetchData();
    // VORHER: [id, isOwnProfile]
    // JETZT: Nur noch id und currentUserId überwachen!
}, [id, currentUserId]);

    if (loading) return <PageLoader message="Profil wird geladen..." />;
    if (error) return <div className="text-center status-box status-error m-4">{error}</div>;
    if (!profile) return <div className="text-center m-4">Profil nicht gefunden.</div>;

    return (
        <div className="app-page">
            <div className="container">
                <div className={styles.actionHeader}>
                    <button onClick={() => navigate('/dashboard')} className={`btn-primary ${styles.backBtn}`}>
                        ← Zurück
                    </button>
                    {isOwnProfile && (
                        <button onClick={() => navigate('/profile/edit')} className={`btn-primary ${styles.editBtn}`}>
                            Profil bearbeiten
                        </button>
                    )}
                </div>
                
                <div className={styles.grid}>
                <div className="card">
                    <div className={styles.profileHeader}>
                        <img 
                            src={profile.profilePicture || `${DEFAULT_AVATAR}${profile.firstName}+${profile.lastName}`} 
                            alt="Profile" 
                            className="avatar-img"
                            style={{ width: '100px', height: '100px' }}
                        />
                        <div>
                            <h1 className="m-0">{profile.firstName} {profile.lastName}</h1>
                            <p className={styles.email}>{profile.email}</p>
                        </div>
                    </div>

                    <hr className={styles.divider} />

                    <div className={styles.infoGrid}>
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
                    <aside className={`card ${styles.courseSection} ${styles.sidebar}`}>
                        <h2 className={styles.courseSectionTitle}>Meine Kurse</h2>
                        <div className={styles.courseGrid}>
                            {courses.length > 0 ? courses.map(course => (
                                <div 
                                    key={course.id} 
                                    onClick={() => navigate(`/courses/${course.id}`)}
                                    className={styles.courseCard}
                                >
                                    <div className={styles.courseTitle}>{course.title}</div>
                                    <div className={styles.courseMeta}>{course.term} {course.academicYear}</div>
                                </div>
                            )) : (
                                <p style={{ color: 'var(--text-muted)' }}>Du bist noch in keinen Kursen eingeschrieben.</p>
                            )}
                        </div>
                    </aside>
                )}
                </div>
            </div>
        </div>
    );
};