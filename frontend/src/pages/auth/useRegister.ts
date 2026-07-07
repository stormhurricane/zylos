import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { userApi } from '../../api/userApi';
import { convertFileToBase64 } from '../../utils/fileUtils';

export const useRegister = () => {
    const [userType, setUserType] = useState<'student' | 'teacher'>('student');
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const [success, setSuccess] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        privateAddress: '',
        studySubject: '',
        chair: '',
        researchArea: '',
        profilePicture: ''
    });

    useEffect(() => {
        if (!success) return;

        const timer = setTimeout(() => {
            navigate('/login');
        }, 3000);

        return () => clearTimeout(timer);
    }, [success, navigate]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        
        if (fieldErrors[name]) {
            setFieldErrors(prev => {
                const { [name]: removed, ...rest } = prev;
                return rest;
            });
        }
        
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;

        if (!file.type.startsWith('image/')) {
            setError('Bitte wähle eine gültige Bilddatei aus.');
            return;
        }

        if (file.size > 1024 * 1024) {
            setError('Das Bild ist zu groß (maximal 1MB erlaubt).');
            return;
        }

        try {
            const base64 = await convertFileToBase64(file);
            setFormData(prev => ({ ...prev, profilePicture: base64 }));
            setError(''); 
        } catch (err) {
            setError('Fehler beim Lesen der Datei.');
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setFieldErrors({});

        const errors: Record<string, string> = {};
        if (!formData.firstName) errors.firstName = 'Vorname ist erforderlich.';
        if (!formData.lastName) errors.lastName = 'Nachname ist erforderlich.';
        if (!formData.email) errors.email = 'E-Mail ist erforderlich.';
        if (formData.password.length < 8) errors.password = 'Passwort muss mind. 8 Zeichen lang sein.';

        if (userType === 'student') {
            if (!formData.studySubject) errors.studySubject = 'Studienfach ist erforderlich.';
        } else {
            if (!formData.chair) errors.chair = 'Lehrstuhl ist erforderlich.';
            if (!formData.researchArea) errors.researchArea = 'Forschungsgebiet ist erforderlich.';
        }

        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors);
            return;
        }

        setIsSubmitting(true);
        
        try {
            if (userType === 'student') {
                const { chair, researchArea, ...studentPayload } = formData;
                await userApi.registerStudent(studentPayload);
            } else {
                const { studySubject, ...teacherPayload } = formData;
                await userApi.registerTeacher(teacherPayload);
            }
            
            setSuccess(true);
        } catch (err: any) {
            setError(err.response?.data?.error || 'Registrierung fehlgeschlagen.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return {
        userType,
        setUserType,
        error,
        fieldErrors,
        success,
        isSubmitting,
        formData,
        handleChange,
        handleFileChange,
        handleSubmit
    };
};