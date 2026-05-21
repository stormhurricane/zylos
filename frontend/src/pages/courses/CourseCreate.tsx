import React, { useState } from 'react';
import { courseApi } from '../../api/courseApi';
import styles from './CourseCreate.module.css';
import { CourseType, SemesterTerm } from '../../api/types'; // Import types for better type safety

export const CourseCreate: React.FC = () => {
    const [manualCourse, setManualCourse] = useState({
        title: '',
        type: CourseType.LECTURE,
        term: SemesterTerm.SUMMER,
        academicYear: ''
    });
    const [csvFile, setCsvFile] = useState<File | null>(null);
    const [status, setStatus] = useState<{ type: 'success' | 'error', text: string } | null>(null);
    const [loadingManual, setLoadingManual] = useState(false);
    const [loadingCsv, setLoadingCsv] = useState(false);

    // Helper to clear status messages after a delay
    const clearStatus = () => {
        setTimeout(() => setStatus(null), 5000); // Clear after 5 seconds
    };

    const handleManualSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoadingManual(true);
        try {
            await courseApi.createCourse(manualCourse);
            setStatus({ type: 'success', text: 'Lehrveranstaltung erfolgreich angelegt!' });
            setManualCourse({ title: '', type: CourseType.LECTURE, term: SemesterTerm.SUMMER, academicYear: '' });
        } catch (err) {
            console.error(err);
            // More specific error handling could be added here based on err.response
            setStatus({ type: 'error', text: `Fehler beim manuellen Anlegen: ${err instanceof Error ? err.message : 'Unbekannter Fehler'}` });
        } finally {
            setLoadingManual(false);
            clearStatus();
        }
    };

    const handleCsvSubmit = async () => {
        if (!csvFile) return;
        setLoadingCsv(true);
        try {
            const res = await courseApi.importCsv(csvFile);
            setStatus({ type: 'success', text: `${res.data.length} Lehrveranstaltungen erfolgreich importiert.` });
            setCsvFile(null);
            // Visually reset the file input
            const fileInput = document.getElementById('csv-file-input') as HTMLInputElement;
            if (fileInput) fileInput.value = '';
        } catch (err) {
            console.error(err);
            setStatus({ type: 'error', text: `Fehler beim CSV-Import. Bitte Dateiformat prüfen: ${err instanceof Error ? err.message : 'Unbekannter Fehler'}` });
        } finally {
            setLoadingCsv(false);
            clearStatus();
        }
    };

    return (
        <div className="app-page">
            <main className="container">
                <h1 className={styles.title}>Lehrveranstaltung verwalten</h1>

                {status && (
                    <div className={`status-box ${status.type === 'success' ? 'status-success' : 'status-error'}`}>
                        {status.text}
                    </div>
                )}

                <div className={styles.grid}>
                    {/* Manual Form */}
                    <section className="card">
                        <h2 className={styles.manualTitle}>Manuelle Eingabe</h2>
                        <form onSubmit={handleManualSubmit}>
                            <div className="form-group">
                                <label htmlFor="manual-title" className="required">Titel</label>
                                <input 
                                    className="form-input"
                                    placeholder="z.B. Software Engineering"
                                    value={manualCourse.title}
                                    onChange={e => setManualCourse({...manualCourse, title: e.target.value})}
                                    id="manual-title" // Added for accessibility
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label htmlFor="manual-type" className="required">Veranstaltungstyp</label>
                                <select 
                                    className="form-input"
                                    value={manualCourse.type as string} // Cast to string for select value
                                    onChange={e => setManualCourse({...manualCourse, type: e.target.value as CourseType})}
                                    id="manual-type" // Added for accessibility
                                >
                                    {Object.values(CourseType).map(type => (
                                        <option key={type} value={type}>{type === CourseType.LECTURE ? 'Vorlesung' : 'Seminar'}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-group">
                                <label htmlFor="manual-term" className="required">Semester</label>
                                <select 
                                    className="form-input"
                                    value={manualCourse.term as string} // Cast to string for select value
                                    onChange={e => setManualCourse({...manualCourse, term: e.target.value as SemesterTerm})}
                                    id="manual-term" // Added for accessibility
                                >
                                    {Object.values(SemesterTerm).map(term => (
                                        <option key={term} value={term}>{term === SemesterTerm.SUMMER ? 'Sommersemester' : 'Wintersemester'}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-group">
                                <label htmlFor="manual-year" className="required">Jahr</label>
                                <input 
                                    className="form-input"
                                    type="text" // Changed to text to allow "2024/25"
                                    pattern="^\d{4}(/\d{2})?$" // Pattern for "YYYY" or "YYYY/YY"
                                    id="manual-year"
                                    placeholder="z.B. 2024"
                                    value={manualCourse.academicYear}
                                    onChange={e => setManualCourse({...manualCourse, academicYear: e.target.value})}
                                    required
                                />
                            </div>
                            <button type="submit" className="btn-primary">Erstellen</button>
                            {loadingManual && <span className="loading-spinner ml-2"></span>}
                        </form>
                        {/* Add a loading spinner or text here if desired */}
                    </section>

                    {/* CSV Import */}
                    <section className="card">
                        <h2 className={styles.csvTitle}>CSV Import</h2>
                        <p className={styles.csvDescription}>
                            Format: Titel;Typ;Semester;Jahr
                        </p>
                        <label htmlFor="csv-file-input" className="sr-only">CSV Import</label>
                            <input 
                            id="csv-file-input"
                            type="file" 
                            accept=".csv"
                            onChange={e => setCsvFile(e.target.files?.[0] || null)}
                            className={styles.fileInput}
                        />
                        <button 
                            onClick={handleCsvSubmit}
                            className={`btn-primary ${styles.csvBtn}`}
                            disabled={!csvFile}
                        >
                            CSV hochladen
                        </button>
                    </section>
                </div>
            </main>
        </div>
    );
};