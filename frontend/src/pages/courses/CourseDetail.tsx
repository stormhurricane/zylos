import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { courseApi, Course, ParticipantsResponse, Material } from '../../api/courseApi';
import api from '../../api/axios';
import { Navbar } from '../../components/Navbar';
import { useAuth } from '../../context/AuthContext';

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
    const [searchResults, setSearchResults] = useState<any[]>([]);
    const { user } = useAuth();
    console.log("Aktueller User aus Context:", user);


    const isInstructor = user && !('matriculationNumber' in user);
    // Prüfe verschiedene gängige Namen für die ID im Auth-Objekt
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
            const res = await api.get('/users/search', { params: { q: query } });
            // Nur Nutzer vorschlagen, die noch nicht im Kurs sind
            const filtered = res.data.filter((u: any) => 
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
            await api.post(`/courses/${courseId}/participants`, { userId: studentId });
            setStudentSearch('');
            setSearchResults([]);
            loadData(); // Liste neu laden
        } catch (err) {
            // alert("Teilnehmer konnte nicht hinzugefügt werden.");
            console.error("Hinzufügen fehlgeschlagen:", err);
            alert(`Fehler 403: Entweder bist du nicht als Lehrkraft für diesen Kurs berechtigt, oder die API-Route ist falsch.`);
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
            // Zurücksetzen des File-Inputs im DOM
            const fileInput = document.getElementById('material-file-input') as HTMLInputElement;
            if (fileInput) fileInput.value = '';
            
            loadData(); // Refresh list
            setTimeout(() => setUploadStatus(null), 3000); // Meldung nach 3s ausblenden
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

    if (loading) return <div className="p-8 text-center">Lade Kursdetails...</div>;
    if (!course) return <div className="p-8 text-center text-red-500">Kurs nicht gefunden.</div>;

    return (
        <div style={{ minHeight: '100vh', backgroundColor: 'var(--color-light)' }}>
            <Navbar />
            <div style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto', display: 'grid', gridTemplateColumns: '1fr 350px', gap: '40px' }}>
                {/* Linke Spalte: Kurs-Info & Materialien */}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '30px' }}>
                    <section>
                        <h1 style={{ fontSize: '2.5rem', marginBottom: '10px' }}>{course.title}</h1>
                        <p style={{ color: 'var(--text-muted)', fontSize: '1.1rem' }}>
                            {course.type === 'LECTURE' ? 'Vorlesung' : 'Seminar'} — {course.term === 'SUMMER' ? 'Sommersemester' : 'Wintersemester'} {course.academicYear}
                        </p>
                    </section>

                    <section className="auth-card" style={{ maxWidth: '100%', padding: '30px' }}>
                        <h2 style={{ marginBottom: '25px', color: 'var(--color-primary)' }}>Lehrmaterialien</h2>
                        
                        {uploadStatus && (
                            <div style={{ 
                                padding: '10px 15px', 
                                marginBottom: '20px', 
                                borderRadius: '6px', 
                                backgroundColor: uploadStatus.type === 'success' ? '#dcfce7' : '#fee2e2',
                                color: uploadStatus.type === 'success' ? '#166534' : '#991b1b',
                                fontSize: '0.9rem'
                            }}>{uploadStatus.text}</div>
                        )}

                        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                            {materials.map(mat => (
                                <div key={mat.id} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '15px', backgroundColor: '#f9fafb', borderRadius: '8px', border: '1px solid #eee' }}>
                                    <div>
                                        <p style={{ fontWeight: '600', margin: 0 }}>{mat.title}</p>
                                        <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', margin: 0 }}>{mat.fileName}</p>
                                    </div>
                                    <button 
                                        onClick={() => handleDownload(mat.id, mat.fileName)}
                                        className="btn-primary"
                                        style={{ width: 'auto', padding: '8px 15px', fontSize: '0.9rem' }}
                                    >
                                        Download
                                    </button>
                                </div>
                            ))}
                            {materials.length === 0 && <p style={{ color: 'var(--text-muted)', fontStyle: 'italic' }}>Noch keine Materialien hochgeladen.</p>}
                        </div>

                        {/* Upload Bereich für Lehrende */}
                        <form onSubmit={handleUpload} style={{ marginTop: '40px', padding: '20px', border: '2px dashed #e2e8f0', borderRadius: '10px' }}>
                            <h3 style={{ fontSize: '1rem', marginBottom: '15px' }}>Material bereitstellen</h3>
                            <div style={{ display: 'flex', gap: '10px' }}>
                                <input 
                                    className="form-input"
                                    placeholder="Titel"
                                    value={uploadTitle}
                                    onChange={e => setUploadTitle(e.target.value)}
                                    style={{ flex: 1 }}
                                />
                                <input 
                                        id="material-file-input"
                                    type="file"
                                    onChange={e => setUploadFile(e.target.files?.[0] || null)}
                                    style={{ fontSize: '0.8rem' }}
                                />
                                <button type="submit" className="btn-primary" style={{ width: 'auto' }}>Hochladen</button>
                            </div>
                        </form>
                    </section>
                </div>

                {/* Rechte Spalte: Teilnehmerliste */}
                <aside style={{ display: 'flex', flexDirection: 'column', gap: '30px' }}>
                    <section className="auth-card" style={{ maxWidth: '100%', padding: '25px' }}>
                        <h3 style={{ marginBottom: '20px', color: 'var(--color-primary)', borderBottom: '1px solid #eee', paddingBottom: '10px' }}>Lehrende</h3>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                            {participants?.instructors.map(prof => (
                                <div key={prof.id} style={{ fontWeight: '600', color: String(prof.id) === String(currentUserId) ? 'var(--color-primary)' : 'inherit' }}>
                                    {prof.firstName} {prof.lastName}{String(prof.id) === String(currentUserId) ? ' (Du)' : ''}
                                </div>
                            ))}
                        </div>

                        <h3 style={{ marginBottom: '20px', marginTop: '30px', color: 'var(--color-secondary)', borderBottom: '1px solid #eee', paddingBottom: '10px' }}>Studierende</h3>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
                            {participants?.students.map(std => (
                                <div key={std.id} style={{ color: String(std.id) === String(currentUserId) ? 'var(--color-primary)' : 'var(--text-main)', fontWeight: String(std.id) === String(currentUserId) ? 'bold' : 'normal' }}>
                                    {std.firstName} {std.lastName}{String(std.id) === String(currentUserId) ? ' (Du)' : ''}
                                </div>
                            ))}
                            {participants?.students.length === 0 && <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>Noch keine Studierenden.</p>}
                        </div>

                        {/* Teilnehmer hinzufügen (Nur für Lehrende) */}
                        {isInstructor && (
                            <div style={{ marginTop: '25px', paddingTop: '20px', borderTop: '1px dashed #eee' }}>
                                <h4 style={{ fontSize: '0.9rem', marginBottom: '10px' }}>Teilnehmer hinzufügen</h4>
                                <input 
                                    className="form-input"
                                    placeholder="Name suchen..."
                                    value={studentSearch}
                                    onChange={e => handleStudentSearch(e.target.value)}
                                    style={{ fontSize: '0.8rem', padding: '8px' }}
                                />
                                {searchResults.length > 0 && (
                                    <div style={{ 
                                        marginTop: '10px', 
                                        backgroundColor: 'white', 
                                        border: '1px solid #eee', 
                                        borderRadius: '6px',
                                        maxHeight: '150px',
                                        overflowY: 'auto'
                                    }}>
                                        {searchResults.map(s => (
                                            <div key={s.id} style={{ 
                                                padding: '8px 12px', 
                                                display: 'flex', 
                                                justifyContent: 'space-between', 
                                                alignItems: 'center',
                                                borderBottom: '1px solid #f9f9f9'
                                            }}>
                                                <span style={{ fontSize: '0.85rem' }}>{s.firstName} {s.lastName}</span>
                                                <button onClick={() => handleAddStudent(s.id)} style={{ background: 'var(--color-secondary)', color: 'white', border: 'none', borderRadius: '4px', padding: '2px 8px', fontSize: '0.75rem', cursor: 'pointer' }}>+</button>
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
