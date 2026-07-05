import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { userApi } from '../../api/userApi';
import { courseApi } from '../../api/courseApi';
import { Course, UserSearchResponse } from '../../api/types';

export const useDashboard = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    const [searchResults, setSearchResults] = useState<UserSearchResponse[]>([]);
    
    const [teachingCourses, setTeachingCourses] = useState<Course[]>([]);
    const [enrolledCourses, setEnrolledCourses] = useState<Course[]>([]);
    
    const [isLoadingCourses, setIsLoadingCourses] = useState<boolean>(false);
    const [searchError, setSearchError] = useState<string | null>(null);

    const performSearch = async (query: string) => {
        try {
            setSearchError(null);
            const response = await userApi.searchUsers(query);
            setSearchResults(response);
        } catch (err) {
            console.error("Suche fehlgeschlagen", err);
            setSearchError("Die Nutzersuche ist fehlgeschlagen. Bitte erneut versuchen.");
        }
    };

    const sortCourses = (courses: Course[]): Course[] => {
        return [...courses].sort((a, b) => {
            const yearA = parseInt(a.academicYear.split('/')[0]);
            const yearB = parseInt(b.academicYear.split('/')[0]);
            if (yearB !== yearA) return yearB - yearA;
            return a.term === 'WINTER' ? -1 : 1;
        });
    };

    const loadAndSortCourses = async () => {
        setIsLoadingCourses(true);
        try {
            const res = await courseApi.getMyCourses();
            setTeachingCourses(sortCourses(res.teachingCourses));
            setEnrolledCourses(sortCourses(res.enrolledCourses));
        } catch (err) {
            console.error("Fehler beim Laden der Kurse", err);
        } finally {
            setIsLoadingCourses(false);
        }
    };

    useEffect(() => {
        const q = searchParams.get('q');
        if (q) {
            performSearch(q);
        } else {
            setSearchResults([]);
        }
        
        loadAndSortCourses();
    }, [searchParams]);

    const handleViewProfile = (userId: number) => {
        navigate(`/profile/${userId}`);
    };

    const handleViewCourse = (courseId: number) => {
        navigate(`/courses/${courseId}`);
    };

    return {
        welcomeName: user?.firstName || 'Nutzer',
        searchResults,
        teachingCourses, 
        enrolledCourses, 
        isLoadingCourses,
        searchError,
        handleViewProfile,
        handleViewCourse
    };
};