import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { courseApi } from '../../api/courseApi';
import { Course, ParticipantsResponse, Material, UserResponse } from '../../api/types';
import { userApi } from '../../api/userApi';
import { useAuth } from '../../context/AuthContext';
import { PageLoader } from '../../components/PageLoader';
import styles from './CourseDetail.module.css';

export const CourseDetail: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const courseId = Number(id);

    const [course, setCourse] = useState<Course | null>(null);
    const [participants, setParticipants] = useState<ParticipantsResponse | null>(null);
    const [materials, setMaterials] = useState<Material[]>([]);
    const [uploadTitle, setUploadTitle] = useState('');
    const [uploadFile, setUploadFile] = useState<File | null>(null);
    const [uploadStatus, setUploadStatus] = useState<{ type: 'success' | 'error', text: string } | null>(null);
    const [loading, setLoading] = useState(true);
    const [studentSearch, setStudentSearch] = useState('');
    const [searchResults, setSearchResults] = useState<UserResponse[]>([]);
    const { user, isInstructor } = useAuth();

    const currentUserId = user?.userId;

    const isEnrolled = currentUserId && (
        participants?.students?.some(s => String(s.id) === String(currentUserId)) || 
        participants?.instructors?.some(i => String(i.id) === String(currentUserId))
    );

    const handleStudentSearch = async (query: string) => {
        setStudentSearch(query);
        if (query.length < 2) {
            setSearchResults([]);
            return;
        }
        try {
            const res = await userApi.searchUsers(query);
            // Only suggest users who are not already enrolled in the course
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
            loadData(); // Refresh data to show the new participant
        } catch (err) {
            console.error("Hinzufügen des Teilnehmers fehlgeschlagen:", err);
            alert(`Error 403: Permission denied or invalid course ID.`);
        }
    };

    const loadData = async () => {
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
    };

    useEffect(() => {
        loadData();
    }, [courseId]);

    const handleUpload = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!uploadFile || !uploadTitle) return;
        try {
            await courseApi.uploadMaterial(courseId, uploadTitle, uploadFile);
            setUploadStatus({ type: 'success', text: 'Material erfolgreich hochgeladen!' });
            setUploadTitle('');
            setUploadFile(null);
            
            // Reset the native file input element
            const fileInput = document.getElementById('material-file-input') as HTMLInputElement;
            if (fileInput) fileInput.value = '';
            
            loadData(); // Reload the materials list
            setTimeout(() => setUploadStatus(null), 3000); // Hide status message after 3 seconds
        } catch (err) {
            setUploadStatus({ type: 'error', text: 'Upload fehlgeschlagen.' });
        }
    };

    const handleDownload = async (materialId: number, fileName: string) => {
        try {
            const response = await courseApi.downloadMaterial(materialId);
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', fileName);
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (err) {
            alert("Download fehlgeschlagen.");
        }
    };

    if (loading) return <PageLoader message="Kursdetails werden geladen..." />;
    if (!course) return <div className="text-center color-error">Kurs nicht gefunden.</div>;

    return (
        <div className="app-page">
            <div className={`container ${styles.container}`}>
                {/* Left Column: Course Information & Materials */}
                <div className={styles.leftColumn}>
                    <section className={styles.header}>
                        <h1>{course.title}</h1>
                        <p className={styles.headerSubtitle}>
                            {course.type === 'LECTURE' ? 'Vorlesung' : 'Seminar'} — {course.term === 'SUMMER' ? 'Sommersemester' : 'Wintersemester'} {course.academicYear}
                        </p>
                    </section>

                    <section className="card">
                        <h2 className={styles.sectionTitle}>Lehrmaterialien</h2>
                        
                        {uploadStatus && (
                            <div className={`status-box ${uploadStatus.type === 'success' ? 'status-success' : 'status-error'}`}>
                                {uploadStatus.text}
                            </div>
                        )}

                        <div className={styles.materialList}>
                            {materials.map(mat => (
                                <div key={mat.id} className={styles.materialItem}>
                                    <div className={styles.materialInfo}>
                                        <p className={styles.materialTitle}>{mat.title}</p>
                                        <p className={styles.materialFileName}>{mat.fileName}</p>
                                    </div>
                                    <button 
                                        onClick={() => handleDownload(mat.id, mat.fileName)}
                                        className="btn-primary"
                                        style={{ width: 'auto' }}
                                    >
                                        Download
                                    </button>
                                </div>
                            ))}
                            {materials.length === 0 && <p className="text-muted italic">Noch keine Materialien hochgeladen.</p>}
                        </div>

                        {/* Material Upload (Visible to instructors only) */}
                        {isInstructor && (
                            <form onSubmit={handleUpload} className={styles.uploadArea}>
                                <h3 className={styles.uploadTitle}>Material bereitstellen</h3>
                                <div className={styles.uploadForm}>
                                    <input 
                                        className="form-input"
                                        placeholder="Titel"
                                        value={uploadTitle}
                                        onChange={e => setUploadTitle(e.target.value)}
                                    />
                                    <input 
                                        id="material-file-input"
                                        type="file"
                                        onChange={e => setUploadFile(e.target.files?.[0] || null)}
                                        className="text-sm"
                                    />
                                    <button type="submit" className="btn-primary" style={{ width: 'auto' }}>Hochladen</button>
                                </div>
                            </form>
                        )}
                    </section>
                </div>
                {/* Right Column: Participant Lists */}
                <aside className={styles.rightColumn}>
                    <section className="card">
                        <h3 className={styles.participantGroupTitle}>Lehrende</h3>
                        <div className={styles.participantList}>
                            {participants?.instructors.map(prof => (
                                <div key={prof.id} className={String(prof.id) === String(currentUserId) ? styles.me : ''}>
                                    {prof.firstName} {prof.lastName}{String(prof.id) === String(currentUserId) ? ' (Du)' : ''}
                                </div>
                            ))}
                        </div>

                        <h3 className={`${styles.participantGroupTitle} ${styles.studentGroupTitle} mt-8`}>Studierende</h3>
                        <div className={styles.participantList}>
                            {participants?.students.map(std => (
                                <div key={std.id} className={String(std.id) === String(currentUserId) ? styles.me : ''}>
                                    {std.firstName} {std.lastName}{String(std.id) === String(currentUserId) ? ' (Du)' : ''}
                                </div>
                            ))}
                            {participants?.students.length === 0 && <p className="text-muted text-sm">Noch keine Studierenden.</p>}
                        </div>

                        {/* Search and add participants (Instructors only) */}
                        {isInstructor && (
                            <div className={styles.addParticipantSection}>
                                <h4 className={styles.addParticipantTitle}>Teilnehmer hinzufügen</h4>
                                <input 
                                    className={`form-input ${styles.addParticipantInput}`}
                                    placeholder="Name suchen..."
                                    value={studentSearch}
                                    onChange={e => handleStudentSearch(e.target.value)}
                                />
                                {searchResults.length > 0 && (
                                    <div className={styles.searchResults}>
                                        {searchResults.map(s => (
                                            <div key={s.id} className={styles.searchResultItem}>
                                                <span className={styles.searchResultName}>{s.firstName} {s.lastName}</span>
                                                <button onClick={() => handleAddStudent(s.id)} className={styles.addButton}>+</button>
                                            </div>
                                        ))}
                                    </div>
                                )}
                            </div>
                        )}
                    </section>
                </aside>
            </div>
        </div>
    );
};
