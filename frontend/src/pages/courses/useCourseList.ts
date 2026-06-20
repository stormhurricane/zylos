import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { courseApi } from '../../api/courseApi';
import { Course } from '../../api/types';

export const useCourseList = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [enrolledIds, setEnrolledIds] = useState<number[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                setError(null);
                const [allRes, myRes] = await Promise.all([
                    courseApi.getAllCourses(),
                    courseApi.getMyCourses()
                ]);
                setCourses(allRes);
                setEnrolledIds(myRes.map(c => c.id));
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
            //  TODO: add clean UI status alerts
            alert('Erfolgreich eingeschrieben!');
        } catch (err) {
            alert('Einschreibung fehlgeschlagen (evtl. bereits eingeschrieben).');
        }
    };

    const handleButtonClick = (courseId: number, isEnrolled: boolean) => {
        if (isEnrolled) {
            navigate(`/courses/${courseId}`);
        } else {
            handleEnroll(courseId);
        }
    };

    return {
        courses,
        enrolledIds,
        loading,
        error,
        handleButtonClick
    };
};