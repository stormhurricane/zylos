import React from 'react';
import styles from './CourseCreate.module.css';
import { CourseType, SemesterTerm } from '../../api/types';
import { useCourseCreate } from './useCourseCreate';

export const CourseCreate = () => {
    const {
        manualCourse,
        setManualCourse,
        csvFile,         // [Certain] WIEDER HINZUGEFÜGT - wird für disabled-State gebraucht
        setCsvFile,      // [Certain] WIEDER HINZUGEFÜGT - wird im onChange aufgerufen
        status,
        loadingManual,
        loadingCsv,
        fileInputRef,
        handleManualSubmit,
        handleCsvSubmit
    } = useCourseCreate();


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
                    <section className="card">
                        <h2 className={styles.manualTitle}>Manuelle Eingabe</h2>
                        <form onSubmit={handleManualSubmit}>
                            {/* ... Deine Form-Inputs bleiben hier, gebunden an setManualCourse ... */}
                            <div className="form-group">
                                <label htmlFor="manual-title" className="required">Titel</label>
                                <input 
                                    className="form-input"
                                    id="manual-title"
                                    required
                                    value={manualCourse.title}
                                    onChange={e => setManualCourse({...manualCourse, title: e.target.value})}
                                />
                            </div>
                            
                            {/* Verfeinerung des Mappings ohne harte String-Casts */}
                            <div className="form-group">
                                <label htmlFor="manual-type" className="required">Veranstaltungstyp</label>
                                <select 
                                    className="form-input"
                                    id="manual-type"
                                    value={manualCourse.type} 
                                    onChange={e => setManualCourse({...manualCourse, type: e.target.value as CourseType})}
                                >
                                    {Object.values(CourseType).map(type => (
                                        <option key={type} value={type}>
                                            {type === CourseType.LECTURE ? 'Vorlesung' : 'Seminar'}
                                        </option>
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
                                    value={manualCourse.academicYear}
                                    onChange={e => setManualCourse({...manualCourse, academicYear: e.target.value})}
                                    required
                                />
                            </div>
                            <button type="submit" className="btn-primary">Erstellen</button>
                            {loadingManual && <span className="loading-spinner ml-2"></span>}
                        </form>
                    </section>

                    <section className="card">
                        <h2 className={styles.csvTitle}>CSV Import</h2>
                        <p className={styles.csvDescription}>Format: Titel;Typ;Semester;Jahr</p>
                        <input 
                            // [Certain] Ref anstelle von ID zugewiesen
                            ref={fileInputRef}
                            id="csv-file-input"
                            type="file" 
                            accept=".csv"
                            onChange={e => setCsvFile(e.target.files?.[0] || null)}
                            className={styles.fileInput}
                        />
                        <button 
                            onClick={handleCsvSubmit}
                            className={`btn-primary ${styles.csvBtn}`}
                            disabled={!csvFile || loadingCsv}
                        >
                            {loadingCsv ? 'Wird hochgeladen...' : 'CSV hochladen'}
                        </button>
                    </section>
                </div>
            </main>
        </div>
    );
};