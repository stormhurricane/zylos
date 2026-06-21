import { PageLoader } from '../../components/PageLoader';
import styles from './Profile.module.css';
import { useProfile } from './useProfile';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Profile = () => {
    const {
        profile,
        courses,
        loading,
        error,
        isOwnProfile,
        handleBack,
        handleEdit,
        handleViewCourse
    } = useProfile();


    if (loading) return <PageLoader message="Profil wird geladen..." />;
    if (error) return <div className="text-center status-box status-error m-4">{error}</div>;
    if (!profile) return <div className="text-center m-4">Profil nicht gefunden.</div>;

    return (
        <div className="app-page">
            <div className="container">
                <div className={styles.actionHeader}>
                    <button onClick={handleBack} className={`btn-primary ${styles.backBtn}`}>
                        ← Zurück
                    </button>
                    {isOwnProfile && (
                        <button onClick={handleEdit} className={`btn-primary ${styles.editBtn}`}>
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

                        {isOwnProfile && (
                            <div className="info-block">
                                <label style={{ fontWeight: 'bold', color: 'var(--text-muted)' }}>Private Adresse</label>
                                <p>{profile.privateAddress || 'Nicht angegeben'}</p>
                            </div>
                        )}
                    </div>
                </div>
                {isOwnProfile && (
                    <aside className={`card ${styles.courseSection} ${styles.sidebar}`}>
                        <h2 className={styles.courseSectionTitle}>Meine Kurse</h2>
                        <div className={styles.courseGrid}>
                            {courses.length > 0 ? courses.map(course => (
                                <div 
                                    key={course.id} 
                                    onClick={() => {handleViewCourse(course.id)}}
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