import React from 'react';
import { PageLoader } from '../../components/PageLoader';
import styles from './CourseDetail.module.css';
import { useCourseDetail } from './useCourseDetail';

export const CourseDetail: React.FC = () => {
    const {
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
    } = useCourseDetail();

    if (loading) return <PageLoader message="Kursdetails werden geladen..." />;
    if (error || !course) return <div className="text-center color-error">Kurs nicht gefunden.</div>;

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
                                        key={fileInputKey}
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
