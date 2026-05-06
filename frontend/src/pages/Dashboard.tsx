import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../api/axios';
import { courseApi, Course } from '../api/courseApi';
import { Navbar } from '../components/Navbar';
import styles from './Dashboard.module.css';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Dashboard = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [searchResults, setSearchResults] = useState<any[]>([]);
    const [myCourses, setMyCourses] = useState<Course[]>([]);

    const performSearch = async (query: string) => {
        try {
            const response = await api.get('/users/search', { params: { q: query } });
            setSearchResults(response.data);
        } catch (err) {
            console.error("Suche fehlgeschlagen", err);
        }
    };

    useEffect(() => {
        const q = searchParams.get('q');
        if (q) performSearch(q);
        
        // Lade meine Kurse
        courseApi.getMyCourses().then(res => {
            // Sortiere anti-chronologisch (Jahr absteigend, Winter vor Sommer)
            const sorted = res.data.sort((a, b) => {
                const yearA = parseInt(a.academicYear.split('/')[0]);
                const yearB = parseInt(b.academicYear.split('/')[0]);
                if (yearB !== yearA) return yearB - yearA;
                return a.term === 'WINTER' ? -1 : 1;
            });
            setMyCourses(sorted);
        }).catch(console.error);
    }, [searchParams]);

    return (
        <div className="app-page">
            <Navbar onSearch={performSearch} />
            
            <main className={styles.main}>
                <header className={styles.header}>
                    <h1>Willkommen, {user?.firstName}!</h1>
                </header>

                <div className={styles.grid}>
                    <div className={styles.content}>
                {/* Suchergebnisse */}
                <section>
                    <h3 className="mb-4">Suche</h3>
                    {searchResults.length > 0 ? (
                        <div className={styles.resultsGrid}>
                            {searchResults.map((res: any, idx: number) => (
                                <div key={res.id || idx} className="card text-center">
                                    <img 
                                        src={res.profilePicture || `${DEFAULT_AVATAR}${res.firstName}+${res.lastName}`} 
                                        alt="Avatar"
                                        className="avatar-img"
                                        style={{ width: '60px', height: '60px', margin: '0 auto var(--spacing-sm)' }}
                                    />
                                    <h4 style={{ margin: '5px 0' }}>{res.firstName} {res.lastName}</h4>
                                    <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{res.studySubject || res.chair || 'Nutzer'}</p>
                                    <button 
                                        onClick={() => navigate(`/profile/${res.id}`)}
                                        className={styles.profileBtn}
                                    >
                                        Profil ansehen
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="card text-center text-muted">
                            Nutze die Suche oben, um Kommilitonen oder Dozenten zu finden.
                        </div>
                    )}
                </section>
                    </div>

                    {/* Sidebar: Meine Kurse */}
                    <aside className={styles.sidebar}>
                        <div className="card">
                            <h3 className="color-primary mb-4">Meine Kurse</h3>
                            <div className="flex flex-col gap-4">
                                {myCourses.length > 0 ? myCourses.map(course => (
                                    <div key={course.id} className={styles.courseItem}>
                                        <div 
                                            onClick={() => navigate(`/courses/${course.id}`)}
                                            className={styles.courseLink}
                                        >
                                            {course.title}
                                        </div>
                                        <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                                            {course.term} {course.academicYear}
                                        </div>
                                    </div>
                                )) : (
                                    <p style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>Du bist noch in keinen Kursen eingeschrieben.</p>
                                )}
                            </div>
                        </div>
                    </aside>
                </div>
            </main>
        </div>
    );
};
