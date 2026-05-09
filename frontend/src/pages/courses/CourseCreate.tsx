import React, { useState } from 'react';
import { courseApi } from '../../api/courseApi';
import styles from './CourseCreate.module.css';

export const CourseCreate: React.FC = () => {
    const [manualCourse, setManualCourse] = useState({
        title: '',
        type: 'LECTURE' as const,
        term: 'SUMMER' as const,
        academicYear: ''
    });
    const [csvFile, setCsvFile] = useState<File | null>(null);
    const [status, setStatus] = useState<{ type: 'success' | 'error', text: string } | null>(null);

    const handleManualSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await courseApi.createCourse(manualCourse);
            setStatus({ type: 'success', text: 'Lehrveranstaltung erfolgreich angelegt!' });
            setManualCourse({ title: '', type: 'LECTURE', term: 'SUMMER', academicYear: '' });
        } catch (err) {
            console.error(err);
            setStatus({ type: 'error', text: 'Fehler beim manuellen Anlegen.' });
        }
    };

    const handleCsvSubmit = async () => {
        if (!csvFile) return;
        try {
            const res = await courseApi.importCsv(csvFile);
            setStatus({ type: 'success', text: `${res.data.length} Lehrveranstaltungen erfolgreich importiert.` });
            setCsvFile(null);
        } catch (err) {
            console.error(err);
            setStatus({ type: 'error', text: 'Fehler beim CSV-Import. Bitte Dateiformat prüfen.' });
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
                                <label className="required">Titel</label>
                                <input 
                                    className="form-input"
                                    placeholder="z.B. Software Engineering"
                                    value={manualCourse.title}
                                    onChange={e => setManualCourse({...manualCourse, title: e.target.value})}
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label className="required">Veranstaltungstyp</label>
                                <select 
                                    className="form-input"
                                    value={manualCourse.type}
                                    onChange={e => setManualCourse({...manualCourse, type: e.target.value as any})}
                                >
                                    <option value="LECTURE">Vorlesung</option>
                                    <option value="SEMINAR">Seminar</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label className="required">Semester</label>
                                <select 
                                    className="form-input"
                                    value={manualCourse.term}
                                    onChange={e => setManualCourse({...manualCourse, term: e.target.value as any})}
                                >
                                    <option value="SUMMER">Sommersemester</option>
                                    <option value="WINTER">Wintersemester</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label className="required">Jahr</label>
                                <input 
                                    className="form-input"
                                    placeholder="z.B. 2024"
                                    value={manualCourse.academicYear}
                                    onChange={e => setManualCourse({...manualCourse, academicYear: e.target.value})}
                                    required
                                />
                            </div>
                            <button type="submit" className="btn-primary">Erstellen</button>
                        </form>
                    </section>

                    {/* CSV Import */}
                    <section className="card">
                        <h2 className={styles.csvTitle}>CSV Import</h2>
                        <p className={styles.csvDescription}>
                            Format: Titel;Typ;Semester;Jahr
                        </p>
                            <input 
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