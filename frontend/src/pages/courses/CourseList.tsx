import React, { useEffect, useState } from 'react';
import { courseApi} from '../../api/courseApi';
import {Course} from '../../api/types';
import { Link, useNavigate } from 'react-router-dom';
import { PageLoader } from '../../components/PageLoader';
import styles from './CourseList.module.css';

export const CourseList: React.FC = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [enrolledIds, setEnrolledIds] = useState<number[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                const [allRes, myRes] = await Promise.all([
                    courseApi.getAllCourses(),
                    courseApi.getMyCourses()
                ]);
                setCourses(allRes.data);
                setEnrolledIds(myRes.data.map(c => c.id));
            } catch (err) {
                console.error("Fehler beim Laden der Kurse", err);
                setError("Fehler beim Laden der Kurse. Bitte versuche es später erneut.");
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const handleEnroll = async (courseId: number) => {
        try {
            await courseApi.enroll(courseId);
            setEnrolledIds(prev => [...prev, courseId]);
            alert('Erfolgreich eingeschrieben!');
        } catch (err) {
            alert('Einschreibung fehlgeschlagen (evtl. bereits eingeschrieben).');
        }
    };

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
                                    onClick={() => isEnrolled ? navigate(`/courses/${course.id}`) : handleEnroll(course.id)}
                                    className={`btn-primary ${styles.enrollBtn} ${isEnrolled ? styles.enrolledBtn : ''}`}
                                >
                                    {isEnrolled ? 'Ansehen' : 'Teilnehmen'}
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