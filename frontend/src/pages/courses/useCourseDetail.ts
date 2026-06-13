import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { courseApi } from '../../api/courseApi';
import { userApi } from '../../api/userApi';
import { useAuth } from '../../context/AuthContext';
import { Course, ParticipantsResponse, Material, UserResponse } from '../../api/types';
import { triggerBinaryDownload } from '../../utils/fileUtils';

export const useCourseDetail = () => {
    const { id } = useParams<{ id: string }>();
    const courseId = Number(id);

    const [course, setCourse] = useState<Course | null>(null);
    const [participants, setParticipants] = useState<ParticipantsResponse | null>(null);
    const [materials, setMaterials] = useState<Material[]>([]);
    const [loading, setLoading] = useState(true);
    
    // Form & Search States
    const [uploadTitle, setUploadTitle] = useState('');
    const [uploadFile, setUploadFile] = useState<File | null>(null);
    const [uploadStatus, setUploadStatus] = useState<{ type: 'success' | 'error', text: string } | null>(null);
    const [studentSearch, setStudentSearch] = useState('');
    const [searchResults, setSearchResults] = useState<UserResponse[]>([]);
    const [fileInputKey, setFileInputKey] = useState(0); 

    const { user, isInstructor } = useAuth();
    const currentUserId = user?.userId;

    const loadData = useCallback(async () => {
        if (isNaN(courseId)) {
            setLoading(false);
            return;
        }
        try {
            const courseRes = await courseApi.getCourseById(courseId);
            setCourse(courseRes.data);

            const [partRes, matRes] = await Promise.allSettled([
                courseApi.getParticipants(courseId),
                courseApi.getMaterials(courseId)
            ]);

            if (partRes.status === 'fulfilled') setParticipants(partRes.value.data);
            if (matRes.status === 'fulfilled') setMaterials(matRes.value.data);
        } catch (err) {
            console.error("Error loading course details", err);
        } finally {
            setLoading(false);
        }
    }, [courseId]);

    useEffect(() => {
        loadData();
    }, [loadData]);

    const handleStudentSearch = async (query: string) => {
        setStudentSearch(query);
        if (query.length < 2) {
            setSearchResults([]);
            return;
        }
        try {
            const res = await userApi.searchUsers(query);
            const filtered = (res.data as unknown as UserResponse[]).filter((u: UserResponse) =>
                !participants?.students?.some(s => s.id === u.id) &&
                !participants?.instructors?.some(i => i.id === u.id)
            );
            setSearchResults(filtered);
        } catch (err) {
            console.error("Suche fehlgeschlagen", err);
        }
    };

    const handleAddStudent = async (studentId: number) => {
        try {
            await courseApi.addParticipant(courseId, studentId);
            setStudentSearch('');
            setSearchResults([]);
            await loadData();
        } catch (err) {
            console.error("Hinzufügen des Teilnehmers fehlgeschlagen:", err);
            alert(`Error 403: Permission denied or invalid course ID.`);
        }
    };

    const handleUpload = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!uploadFile || !uploadTitle) return;
        try {
            await courseApi.uploadMaterial(courseId, uploadTitle, uploadFile);
            setUploadStatus({ type: 'success', text: 'Material erfolgreich hochgeladen!' });
            setUploadTitle('');
            setUploadFile(null);
            setFileInputKey(prev => prev + 1); 
            
            await loadData();
            setTimeout(() => setUploadStatus(null), 3000);
        } catch (err) {
            setUploadStatus({ type: 'error', text: 'Upload fehlgeschlagen.' });
        }
    };

    const handleDownload = async (materialId: number, fileName: string) => {
    try {
        const response = await courseApi.downloadMaterial(materialId);
        triggerBinaryDownload(new Blob([response.data]), fileName);
    } catch (err) {
        alert("Download fehlgeschlagen.");
    }
};

    return {
        course,
        participants,
        materials,
        loading,
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