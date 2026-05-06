import React, { useEffect, useState } from 'react';
import { courseApi, Course } from '../../api/courseApi';
import { Link, useNavigate } from 'react-router-dom';
import { Navbar } from '../../components/Navbar';
import styles from './CourseList.module.css';

export const CourseList: React.FC = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [enrolledIds, setEnrolledIds] = useState<number[]>([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [allRes, myRes] = await Promise.all([
                    courseApi.getAllCourses(),
                    courseApi.getMyCourses()
                ]);
                setCourses(allRes.data);
                setEnrolledIds(myRes.data.map(c => c.id));
            } catch (err) {
                console.error("Fehler beim Laden der Kurse", err);
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

    return (
        <div className="app-page">
            <Navbar />
            <div className="container">
                <h1 className={styles.title}>Verfügbare Lehrveranstaltungen</h1>
                <div className={styles.grid}>
                    {courses.map(course => {
                        const isEnrolled = enrolledIds.includes(course.id);
                        return (
                            <div key={course.id} className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
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