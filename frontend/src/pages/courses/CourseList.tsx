import React from 'react';
import { Link } from 'react-router-dom';
import { PageLoader } from '../../components/PageLoader';
import { useCourseList } from './useCourseList';
import styles from './CourseList.module.css';

export const CourseList: React.FC = () => {
    const { courses, enrolledIds, loading, error, handleButtonClick } = useCourseList();

    if (loading) return <PageLoader message="Loading courses..." />;

    return (
        <div className="app-page">
            <div className="container">
                <h1 className={styles.title}>Verfügbare Lehrveranstaltungen</h1>
                
                {error && <div className="status-box status-error">{error}</div>}

                <div className={styles.grid}>
                    {courses.map(course => {
                        const isEnrolled = enrolledIds.includes(course.id);
                        return (
                            <div key={course.id} className={`card ${styles.courseCard}`}>
                                <div className={styles.courseHeader}>
                                    <Link to={`/courses/${course.id}`} className={styles.courseLink}>
                                        {course.title}
                                    </Link>
                                    <p className={styles.courseType}>
                                        {course.type === 'LECTURE' ? 'Vorlesung' : 'Seminar'}
                                    </p>
                                    <p className={styles.courseMeta}>
                                        {course.term === 'SUMMER' ? 'SoSe' : 'WiSe'} {course.academicYear}
                                    </p>
                                </div>
                                <button 
                                    onClick={() => handleButtonClick(course.id, isEnrolled)}
                                    className={`btn-primary ${styles.enrollBtn} ${isEnrolled ? styles.enrolledBtn : ''}`}
                                >
                                    {isEnrolled ? 'Ansehen' : 'Einschreiben'}
                                </button>
                            </div>
                        );
                    })}
                </div>
                
                {courses.length === 0 && (
                    <div className="card text-center text-muted">
                        Keine Lehrveranstaltungen gefunden.
                    </div>
                )}
            </div>
        </div>
    );
};