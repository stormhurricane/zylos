import { useAuth } from '../../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { useDashboard } from './useDashboard';
import styles from './Dashboard.module.css';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Dashboard = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    
    // Custom Hook for Dashboard Logic
    const { searchResults, myCourses, isLoadingCourses, searchError } = useDashboard();

    return (
        <div className="app-page">
            <main className={styles.main}>
                <header className={styles.header}>
                    <h1>Willkommen, {user?.firstName}!</h1>
                </header>

                <div className={styles.grid}>
                    <div className={styles.content}>
                        {/* Search Results */}
                        <section>
                            <h3 className="mb-4">Suche</h3>
                            {searchError && <div className="alert alert-danger">{searchError}</div>}
                            
                            {searchResults.length > 0 ? (
                                <div className={styles.resultsGrid}>
                                    {searchResults.map((res) => (
                                        <div key={res.id} className="card text-center">
                                            <img 
                                                src={res.profilePicture || `${DEFAULT_AVATAR}${res.firstName}+${res.lastName}`} 
                                                alt="Avatar"
                                                className="avatar-img"
                                                style={{ width: '60px', height: '60px', margin: '0 auto var(--spacing-sm)' }}
                                            />
                                            <h4 style={{ margin: '5px 0' }}>{res.firstName} {res.lastName}</h4>
                                            <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                                                {res.studySubject || res.chair || 'Nutzer'}
                                            </p>
                                            <button 
                                                onClick={() => navigate(`/profile/${res.id}`)}
                                                className={styles.profileBtn}
                                            >
                                                Profil ansehen
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="card text-center text-muted">
                                    Nutze die Suche oben, um Kommilitonen oder Dozenten zu finden.
                                </div>
                            )}
                        </section>
                    </div>

                    {/* Sidebar: My Courses */}
                    <aside className={styles.sidebar}>
                        <div className="card">
                            <h3 className="color-primary mb-4">Meine Kurse</h3>
                            <div className="flex flex-col gap-4">
                                {isLoadingCourses ? (
                                    <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>Kurse werden geladen...</p>
                                ) : myCourses.length > 0 ? (
                                    myCourses.map(course => (
                                        <div key={course.id} className={styles.courseItem}>
                                            <div 
                                                onClick={() => navigate(`/courses/${course.id}`)}
                                                className={styles.courseLink}                                            >
                                                {course.title}
                                            </div>
                                            <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                                                {course.term} {course.academicYear}
                                            </div>
                                        </div>
                                    ))
                                ) : (
                                    <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>
                                        Du bist noch in keinen Kursen eingeschrieben.
                                    </p>
                                )}
                            </div>
                        </div>
                    </aside>
                </div>
            </main>
        </div>
    );
};