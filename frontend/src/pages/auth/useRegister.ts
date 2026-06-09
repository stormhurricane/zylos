import { useState } from 'react';
import { userApi } from '../../api/userApi';
import { convertFileToBase64 } from '../../utils/fileUtils';

export const useRegister = () => {
    const [userType, setUserType] = useState<'student' | 'teacher'>('student');
    const [error, setError] = useState('');
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
    const [success, setSuccess] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);

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

    // handle input changes
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        
        if (fieldErrors[name]) {
            setFieldErrors(prev => {
                const { [name]: removed, ...rest } = prev;
                return rest;
            });
        }
        
        // BEST PRACTICE: use functional update to ensure we always have the latest state, especially important if multiple changes happen in quick succession
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    // handle file upload
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
            // use utility function to convert file to base64, this keeps the hook clean and focused on logic
            const base64 = await convertFileToBase64(file);
            setFormData({ ...formData, profilePicture: base64 });
            setError(''); // Eventuelle vorherige Fehler löschen
        } catch (err) {
            setError('Fehler beim Lesen der Datei.');
        }
    };

    // send form
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
            // use destructuring to avoid sending unnecessary fields to the API, this keeps our payload clean and focused on the user type
            if (userType === 'student') {
                const { chair, researchArea, ...studentPayload } = formData;
                await userApi.registerStudent(studentPayload);
            } else {
                const { studySubject, ...teacherPayload } = formData;
                await userApi.registerTeacher(teacherPayload);
            }
            
            setSuccess(true);
            
            // WICHTIG: Das Timeout lagern wir NICHT hier stumpf als Seiteneffekt旧 ein,
            // sondern überlassen das der View oder einem sauberen useEffect, falls nötig.
            // Fürs Erste triggern wir nur den Erfolg.
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