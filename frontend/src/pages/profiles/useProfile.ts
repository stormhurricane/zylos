import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { userApi } from '../../api/userApi';
import { Course, ProfileResponse } from '../../api/types';
import { courseApi } from '../../api/courseApi';

export const useProfile = () => {
    const { id: urlId } = useParams<{ id: string }>();
    const navigate = useNavigate();
    
    const { user } = useAuth();
    const currentUserId = user?.userId;

    const targetUserId = urlId ? Number(urlId) : currentUserId;

    const [profile, setProfile] = useState<ProfileResponse | null>(null);
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const isOwnProfile = !urlId || Number(urlId) === currentUserId;
    
    useEffect(() => {
        if (!targetUserId) return;

        const fetchData = async () => {
            try {
                setLoading(true);
                setError(null);
                
                const profileData = await userApi.getProfile(targetUserId);
                setProfile(profileData);

                if (isOwnProfile && profileData) {
                    const coursesRes = await courseApi.getMyCourses();
                    setCourses(coursesRes);
                } else {
                    setCourses([]);
                }
            } catch (err) {
                console.error("Schwerwiegender Fehler beim Laden des Profils", err);
                setError("Das Profil konnte aufgrund eines Serverfehlers nicht geladen werden.");
            } finally {
                setLoading(false);
            }
        };
        
        fetchData();

    }, [targetUserId, isOwnProfile]);

    const handleBack = () => navigate('/dashboard');
    const handleEdit = () => navigate('/profile/edit');
    const handleViewCourse = (courseId: number) => navigate(`/courses/${courseId}`);

    return {
        profile,
        courses,
        loading,
        error,
        isOwnProfile,
        handleBack,
        handleEdit,
        handleViewCourse
    };
};