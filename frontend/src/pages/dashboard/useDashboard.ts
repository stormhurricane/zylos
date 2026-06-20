import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { userApi } from '../../api/userApi';
import { courseApi } from '../../api/courseApi';
import { Course, ProfileResponse } from '../../api/types';

export const useDashboard = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    const [searchResults, setSearchResults] = useState<ProfileResponse[]>([]);
    const [myCourses, setMyCourses] = useState<Course[]>([]);
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

    const loadAndSortCourses = async () => {
        setIsLoadingCourses(true);
        try {
            const res = await courseApi.getMyCourses();
            
            const sorted = res.sort((a, b) => {
                const yearA = parseInt(a.academicYear.split('/')[0]);
                const yearB = parseInt(b.academicYear.split('/')[0]);
                if (yearB !== yearA) return yearB - yearA;
                return a.term === 'WINTER' ? -1 : 1;
            });
            
            setMyCourses(sorted);
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
        myCourses,
        isLoadingCourses,
        searchError,
        handleViewProfile,
        handleViewCourse
    };
};