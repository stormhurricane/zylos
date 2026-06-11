import { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { userApi } from '../../api/userApi';
import { Course, ProfileResponse } from '../../api/types';
import { courseApi } from '../../api/courseApi';

export const useProfile = (id: string | undefined) => {
    // States
    const [profile, setProfile] = useState<ProfileResponse | null>(null);
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    // Context 
    const { user } = useAuth();
    const currentUserId = user?.userId;

    // Derived State (Synchron calculated on every render, no need for useState)
    const isOwnProfile = !id || (currentUserId !== undefined && String(id) === String(currentUserId));

    useEffect(() => {
        const fetchData = async () => {
            try {
                setLoading(true);
                setError(null);
                
                // 1: load profile data (depends directly on 'id')
                const profileData = await userApi.getProfile(id);
                setProfile(profileData);

                // 2 load own courses (depends on 'isOwnProfile', which in turn depends on 'currentUserId' and 'id')
                if (isOwnProfile && profileData) {
                    const coursesRes = await courseApi.getMyCourses();
                    setCourses(coursesRes.data);
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

    }, [id, isOwnProfile]);

    return {
        profile,
        courses,
        loading,
        error,
        isOwnProfile
    };
};