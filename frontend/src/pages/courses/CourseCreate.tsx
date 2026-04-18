import React, { useState } from 'react';
import { courseApi } from '../../api/courseApi';
import { Navbar } from '../../components/Navbar';

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
        <div style={{ minHeight: '100vh', backgroundColor: 'var(--color-light)' }}>
            <Navbar />
            <main style={{ padding: '40px', maxWidth: '1200px', margin: '0 auto' }}>
                <h1 style={{ marginBottom: '40px' }}>Lehrveranstaltung verwalten</h1>

                {status && (
                    <div className="auth-card" style={{ 
                        padding: '15px', 
                        marginBottom: '20px', 
                        borderRadius: '8px', 
                        backgroundColor: status.type === 'success' ? '#dcfce7' : '#fee2e2',
                        color: status.type === 'success' ? '#166534' : '#991b1b',
                        fontWeight: '500'
                    }}>
                        {status.text}
                    </div>
                )}

                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '40px' }}>
                    {/* Manual Form */}
                    <section className="auth-card" style={{ maxWidth: '100%' }}>
                        <h2 style={{ marginBottom: '20px', color: 'var(--color-primary)' }}>Manuelle Eingabe</h2>
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
                    <section className="auth-card" style={{ maxWidth: '100%' }}>
                        <h2 style={{ marginBottom: '10px', color: 'var(--color-secondary)' }}>CSV Import</h2>
                        <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem', marginBottom: '20px' }}>
                            Format: Titel;Typ;Semester;Jahr
                        </p>
                            <input 
                            type="file" 
                            accept=".csv"
                            onChange={e => setCsvFile(e.target.files?.[0] || null)}
                            style={{ marginBottom: '20px', width: '100%' }}
                        />
                        <button 
                            onClick={handleCsvSubmit}
                            className="btn-primary"
                            style={{ background: 'var(--color-secondary)' }}
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