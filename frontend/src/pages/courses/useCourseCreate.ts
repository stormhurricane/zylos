import { useState, useRef, useEffect } from 'react';
import { courseApi } from '../../api/courseApi';
import { CourseType, SemesterTerm } from '../../api/types';

export const useCourseCreate = () => {
    const [manualCourse, setManualCourse] = useState({
        title: '',
        type: CourseType.LECTURE,
        term: SemesterTerm.SUMMER,
        academicYear: ''
    });
    const [csvFile, setCsvFile] = useState<File | null>(null);
    const [status, setStatus] = useState<{ type: 'success' | 'error'; text: string } | null>(null);
    const [loadingManual, setLoadingManual] = useState(false);
    const [loadingCsv, setLoadingCsv] = useState(false);

    const timeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    const fileInputRef = useRef<HTMLInputElement | null>(null);

    const triggerStatus = (type: 'success' | 'error', text: string) => {
        setStatus({ type, text });
        if (timeoutRef.current) clearTimeout(timeoutRef.current);
        timeoutRef.current = setTimeout(() => setStatus(null), 5000);
    };

    const handleManualSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoadingManual(true);
        try {
            await courseApi.createCourse(manualCourse);
            triggerStatus('success', 'Lehrveranstaltung erfolgreich angelegt!');
            setManualCourse({ title: '', type: CourseType.LECTURE, term: SemesterTerm.SUMMER, academicYear: '' });
        } catch (err) {
            console.error(err);
            triggerStatus('error', `Fehler beim manuellen Anlegen.`);
        } finally {
            setLoadingManual(false);
        }
    };

    const handleCsvSubmit = async () => {
        if (!csvFile) return;
        setLoadingCsv(true);
        try {
            const res = await courseApi.importCsv(csvFile);
            triggerStatus('success', `${res.length} Lehrveranstaltungen erfolgreich importiert.`);
            setCsvFile(null);
            
            if (fileInputRef.current) fileInputRef.current.value = '';
        } catch (err) {
            console.error(err);
            triggerStatus('error', 'Fehler beim CSV-Import. Bitte Dateiformat prüfen.');
        } finally {
            setLoadingCsv(false);
        }
    };

    useEffect(() => {
        return () => {
            if (timeoutRef.current) clearTimeout(timeoutRef.current);
        };
    }, []);

    return {
        manualCourse,
        setManualCourse, 
        csvFile,
        setCsvFile,
        status,
        loadingManual,   
        loadingCsv,     
        fileInputRef,
        handleManualSubmit,
        handleCsvSubmit
    };
};