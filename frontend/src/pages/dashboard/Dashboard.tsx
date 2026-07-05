import { useDashboard } from './useDashboard';
import styles from './Dashboard.module.css';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Dashboard = () => {
    const {
        welcomeName,
        searchResults,
        teachingCourses,
        enrolledCourses,
        isLoadingCourses,
        searchError,
        handleViewProfile,
        handleViewCourse
    } = useDashboard();

    const hasNoCourses = (!teachingCourses || teachingCourses.length === 0) && (!enrolledCourses ||enrolledCourses.length === 0);

    return (
        <div className="app-page">
            <main className={styles.main}>
                <header className={styles.header}>
                    <h1>Willkommen, {welcomeName}!</h1>
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
                                                className={styles.avatarImg}
                                            />
                                            <h4 className={styles.searchResultTitle}>{res.firstName} {res.lastName}</h4>
                                            <p className={styles.searchResultSub}>
                                                {res.subInfo || ''}
                                            </p>
                                            <button 
                                                onClick={() => handleViewProfile(res.id)}
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
                            <div className="flex flex-col">
                                {isLoadingCourses ? (
                                    <p className={styles.loadingText}>Kurse werden geladen...</p>
                                ) : hasNoCourses ? (
                                    <p className={styles.emptyText}>
                                        Du bist noch in keinen Kursen eingetragen.
                                    </p>
                                ) : (
                                    <>
                                        {/* Sektion für Lehrende */}
                                        {teachingCourses.length > 0 && (
                                            <div>
                                                <h4 className={styles.sectionHeading}>MEINE LEHRE</h4>
                                                <div className="flex flex-col">
                                                    {teachingCourses.map(course => (
                                                        <div key={course.id} className={styles.courseItem}>
                                                            <div 
                                                                onClick={() => handleViewCourse(course.id)}
                                                                className={styles.courseLink}
                                                            >
                                                                {course.title}
                                                            </div>
                                                            <div className={styles.courseItemMeta}>
                                                                {course.term} {course.academicYear}
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                        )}

                                        {/* Sektion für Belegte Kurse */}
                                        {enrolledCourses.length > 0 && (
                                            <div className={teachingCourses.length > 0 ? styles.courseGroup : undefined}>
                                                <h4 className={styles.sectionHeading}>BELEGTE KURSE</h4>
                                                <div className="flex flex-col">
                                                    {enrolledCourses.map(course => (
                                                        <div key={course.id} className={styles.courseItem}>
                                                            <div 
                                                                onClick={() => handleViewCourse(course.id)}
                                                                className={styles.courseLink}
                                                            >
                                                                {course.title}
                                                            </div>
                                                            <div className={styles.courseItemMeta}>
                                                                {course.term} {course.academicYear}
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                        )}
                                    </>
                                )}
                            </div>
                        </div>
                    </aside>
                </div>
            </main>
        </div>
    );
};