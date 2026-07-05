import { PageLoader } from '../../components/PageLoader';
import styles from './Profile.module.css';
import { useProfile } from './useProfile';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Profile = () => {
    const {
        profile,
        teachingCourses,
        enrolledCourses,
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
                                className={styles.avatarImg}
                            />
                            <div>
                                <h1 className="m-0">{profile.firstName} {profile.lastName}</h1>
                                <p className={styles.email}>{profile.email}</p>
                            </div>
                        </div>

                        <hr className={styles.divider} />

                        <div className={styles.infoGrid}>
                            <div className="info-block">
                                <label className={styles.infoBlockLabel}>Rolle / Details</label>
                                {profile.matriculationNumber ? (
                                    <p>Student (Matrikelnr: {profile.matriculationNumber})</p>
                                ) : (
                                    <p>Lehrender</p>
                                )}
                            </div>
                            
                            {profile.matriculationNumber ? (
                                <div className="info-block">
                                    <label className={styles.infoBlockLabel}>Studienfach</label>
                                    <p>{profile.studySubject || 'Nicht angegeben'}</p>
                                </div>
                            ) : (
                                <>
                                    <div className="info-block">
                                        <label className={styles.infoBlockLabel}>Lehrstuhl</label>
                                        <p>{profile.chair || 'Nicht angegeben'}</p>
                                    </div>

                                    <div className="info-block">
                                        <label className={styles.infoBlockLabel}>Forschungsgebiet</label>
                                        <p>{profile.researchArea || 'Nicht angegeben'}</p>
                                    </div>
                                </>
                            )}

                            {isOwnProfile && (
                                <div className="info-block">
                                    <label className={styles.infoBlockLabel}>Private Adresse</label>
                                    <p>{profile.privateAddress || 'Nicht angegeben'}</p>
                                </div>
                            )}
                        </div>
                    </div>

                    {isOwnProfile && (
                        <aside className={`card ${styles.courseSection} ${styles.sidebar}`}>
                            <h2 className={styles.courseSectionTitle}>Meine Kurse</h2>
                            <div className={styles.courseGrid}>
                                
                                {/* Sektion: Eigene Lehre */}
                                {teachingCourses && teachingCourses.length > 0 && (
                                    <div className={styles.courseGroup}>
                                        <h4 className={styles.sectionHeading}>Eigene Lehre</h4>
                                        {teachingCourses.map(course => (
                                            <div 
                                                key={course.id} 
                                                onClick={() => handleViewCourse(course.id)}
                                                className={styles.courseCard}
                                            >
                                                <div className={styles.courseTitle}>{course.title}</div>
                                                <div className={styles.courseMeta}>{course.term} {course.academicYear}</div>
                                            </div>
                                        ))}
                                    </div>
                                )}

                                {/* Sektion: Belegte Kurse */}
                                {enrolledCourses &&enrolledCourses.length > 0 && (
                                    <div className={styles.courseGroup}>
                                        <h4 className={styles.sectionHeading}>Belegte Kurse</h4>
                                        {enrolledCourses.map(course => (
                                            <div 
                                                key={course.id} 
                                                onClick={() => handleViewCourse(course.id)}
                                                className={styles.courseCard}
                                            >
                                                <div className={styles.courseTitle}>{course.title}</div>
                                                <div className={styles.courseMeta}>{course.term} {course.academicYear}</div>
                                            </div>
                                        ))}
                                    </div>
                                )}

                                {/* Fallback: Gar keine Kurse */}
                                {(!teachingCourses ||teachingCourses.length === 0) && (!enrolledCourses || enrolledCourses.length === 0) && (
                                    <p className={styles.emptyText}>Du bist noch in keinen Kursen eingetragen.</p>
                                )}
                                
                            </div>
                        </aside>
                    )}
                </div>
            </div>
        </div>
    );
};