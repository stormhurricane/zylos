import React, { useEffect, useState } from 'react';
import { courseApi, Course } from '../../api/courseApi';
import { Link, useNavigate } from 'react-router-dom';
import { Navbar } from '../../components/Navbar';

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
        <div style={{ minHeight: '100vh', backgroundColor: 'var(--color-light)' }}>
            <Navbar />
            <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto' }}>
                <h1 style={{ marginBottom: '30px' }}>Verfügbare Lehrveranstaltungen</h1>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '25px' }}>
                    {courses.map(course => {
                        const isEnrolled = enrolledIds.includes(course.id);
                        return (
                            <div key={course.id} className="auth-card" style={{ maxWidth: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                                <div style={{ marginBottom: '20px' }}>
                                    <Link to={`/courses/${course.id}`} style={{ fontSize: '1.4rem', fontWeight: 'bold', color: 'var(--color-primary)', textDecoration: 'none' }}>
                                        {course.title}
                                    </Link>
                                    <p style={{ marginTop: '10px', fontWeight: '600', color: 'var(--text-muted)', fontSize: '0.85rem', textTransform: 'uppercase' }}>
                                        {course.type === 'LECTURE' ? 'Vorlesung' : 'Seminar'}
                                    </p>
                                    <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
                                        {course.term === 'SUMMER' ? 'SoSe' : 'WiSe'} {course.academicYear}
                                    </p>
                                </div>
                                <button 
                                    onClick={() => isEnrolled ? navigate(`/courses/${course.id}`) : handleEnroll(course.id)}
                                    className="btn-primary"
                                    style={{ 
                                        width: '100%', 
                                        backgroundColor: isEnrolled ? 'var(--color-secondary)' : 'var(--color-primary)' 
                                    }}
                                >
                                    {isEnrolled ? 'Ansehen' : 'Teilnehmen'}
                                </button>
                            </div>
                        );
                    })}
                </div>
                {courses.length === 0 && (
                    <div className="auth-card" style={{ textAlign: 'center', padding: '40px', color: 'var(--text-muted)' }}>
                        Keine Lehrveranstaltungen gefunden.
                    </div>
                )}
            </div>
        </div>
    );
};