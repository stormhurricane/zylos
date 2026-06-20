import { useState, useEffect, useCallback, useRef } from 'react';
import { useParams } from 'react-router-dom';
import { courseApi } from '../../api/courseApi';
import { userApi } from '../../api/userApi';
import { useAuth } from '../../context/AuthContext';
import { Course, ParticipantsResponse, Material, UserResponse } from '../../api/types';
import { triggerBinaryDownload } from '../../utils/fileUtils';

// TODO refactor into three seperate hooks

export const useCourseDetail = () => {
    const { id } = useParams<{ id: string }>();
    
    const courseId = id ? Number(id) : NaN;

    const [course, setCourse] = useState<Course | null>(null);
    const [participants, setParticipants] = useState<ParticipantsResponse | null>(null);
    const [materials, setMaterials] = useState<Material[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    
    const [uploadTitle, setUploadTitle] = useState('');
    const [uploadFile, setUploadFile] = useState<File | null>(null);
    const [uploadStatus, setUploadStatus] = useState<{ type: 'success' | 'error', text: string } | null>(null);
    const [studentSearch, setStudentSearch] = useState('');
    const [searchResults, setSearchResults] = useState<UserResponse[]>([]);
    const [fileInputKey, setFileInputKey] = useState(0); 

    const { user, isInstructor } = useAuth();
    const currentUserId = user?.userId;
    
    const timeoutRef = useRef<NodeJS.Timeout | null>(null);

    const loadData = useCallback(async () => {
        if (!id || isNaN(courseId)) {
            setError('Ungültige Kurs-ID angegeben.');
            setLoading(false);
            return;
        }
        
        try {
            setLoading(true);
            setError(null);

            const courseRes = await courseApi.getCourseById(courseId);
            setCourse(courseRes);

            const [partRes, matRes] = await Promise.allSettled([
                courseApi.getParticipants(courseId),
                courseApi.getMaterials(courseId)
            ]);

            if (partRes.status === 'fulfilled') setParticipants(partRes.value);
            if (matRes.status === 'fulfilled') setMaterials(matRes.value);
        } catch (err: any) {
            console.error("Error loading course details", err);
            setError(err.response?.data?.error || 'Fehler beim Laden der Kursdetails.');
        } finally {
            setLoading(false);
        }
    }, [id]); 

    useEffect(() => {
        loadData();
        
        return () => {
            if (timeoutRef.current) clearTimeout(timeoutRef.current);
        };
    }, [loadData]);

    const handleStudentSearch = async (query: string) => {
        setStudentSearch(query);
        if (query.length < 2) {
            setSearchResults([]);
            return;
        }
        try {
            const res = await userApi.searchUsers(query);
            const filtered = (res as unknown as UserResponse[]).filter((u: UserResponse) =>
                !participants?.students?.some(s => s.id === u.id) &&
                !participants?.instructors?.some(i => i.id === u.id)
            );
            setSearchResults(filtered);
        } catch (err) {
            console.error("Suche fehlgeschlagen", err);
        }
    };

    const handleAddStudent = async (studentId: number) => {
        if (isNaN(courseId)) return;
        
        try {
            await courseApi.addParticipant(courseId, studentId);
            setStudentSearch('');
            setSearchResults([]);
            await loadData();
        } catch (err) {
            console.error("Hinzufügen des Teilnehmers fehlgeschlagen:", err);
            alert(`Aktion fehlgeschlagen: Keine Berechtigung oder ungültige ID.`);
        }
    };

    const handleUpload = async (e: React.FormEvent) => {
        e.preventDefault();
        if (isNaN(courseId) || !uploadFile || !uploadTitle) return;
        
        try {
            await courseApi.uploadMaterial(courseId, uploadTitle, uploadFile);
            setUploadStatus({ type: 'success', text: 'Material erfolgreich hochgeladen!' });
            setUploadTitle('');
            setUploadFile(null);
            setFileInputKey(prev => prev + 1); 
            
            await loadData();
            
            if (timeoutRef.current) clearTimeout(timeoutRef.current);
            timeoutRef.current = setTimeout(() => setUploadStatus(null), 3000);
        } catch (err) {
            setUploadStatus({ type: 'error', text: 'Upload fehlgeschlagen.' });
        }
    };

    const handleDownload = async (materialId: number, fileName: string) => {
        try {
            const response = await courseApi.downloadMaterial(materialId);
            triggerBinaryDownload(new Blob([response]), fileName);
        } catch (err) {
            alert("Download fehlgeschlagen.");
        }
    };

    return {
        course,
        participants,
        materials,
        loading,
        error,
        uploadTitle,
        uploadStatus,
        studentSearch,
        searchResults,
        isInstructor,
        currentUserId,
        fileInputKey,
        setUploadTitle,
        setUploadFile,
        handleStudentSearch,
        handleAddStudent,
        handleUpload,
        handleDownload
    };
};