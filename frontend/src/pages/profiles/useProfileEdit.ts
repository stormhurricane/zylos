import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userApi } from '../../api/userApi';
import { ProfileUpdateRequest } from '../../api/types';
import { convertFileToBase64 } from '../../utils/fileUtils';

export const useProfileEdit = () => {
    const [formData, setFormData] = useState<ProfileUpdateRequest>({
        password: '',
        privateAddress: '',
        profilePicture: '',
        chair: '',
        researchArea: '',
        studySubject: ''
    });
    const [displayName, setDisplayName] = useState({ firstName: '', lastName: '' });
    const [isStudent, setIsStudent] = useState(false);
    const [loading, setLoading] = useState(true);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const profile = await userApi.getProfile();
                
                if (!profile) {
                    setError('Profil nicht gefunden.');
                    return;
                }
                
                setDisplayName({ firstName: profile.firstName, lastName: profile.lastName });
                
                setFormData({
                    password: '', 
                    privateAddress: profile.privateAddress || '',
                    profilePicture: profile.profilePicture || '',
                    studySubject: profile.studySubject || '',
                    chair: profile.chair || '',
                    researchArea: profile.researchArea || ''
                });
                
                setIsStudent(!!profile.matriculationNumber);
            } catch (err) {
                console.error("Fehler beim Laden des Edit-Profils", err);
                setError('Profil konnte nicht geladen werden.');
            } finally {
                setLoading(false);
            }
        };
        fetchProfile();
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        try {
            const base64 = await convertFileToBase64(file);
            setFormData(prev => ({ ...prev, profilePicture: base64 }));
        } catch (err) {
            setError('Bildverarbeitung fehlgeschlagen.');
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);
        setError('');

        try {
            await userApi.updateProfile(formData);
            navigate('/profile');
        } catch (err) {
            console.error("Profil-Update fehlgeschlagen", err);
            setError('Update fehlgeschlagen.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return {
        formData,
        displayName,
        isStudent,
        loading,
        isSubmitting,
        error,
        handleChange,
        handleFileChange,
        handleSubmit,
        cancel: () => navigate('/profile')
    };
};