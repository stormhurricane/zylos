import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './Navbar.module.css';

export const Navbar: React.FC = () => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        navigate(`/dashboard?q=${encodeURIComponent(searchQuery)}`);
    };

    // Lehrende haben keine Matrikelnummer, Studierende hingegen schon.
    // Diese Logik folgt der Implementierung in Profile.tsx.
    const isInstructor = user && !('matriculationNumber' in user);

    return (
        <nav className={styles.nav}>
            <div className={styles.linksContainer}>
                <Link to="/dashboard" style={{ textDecoration: 'none' }}>
                    <h2 className={styles.brand}>Zylos</h2>
                </Link>
                
                <div className={styles.links}>
                    <Link to="/courses" className={styles.link}>Alle Kurse</Link>
                    {isInstructor && (
                        <Link to="/courses/new" className={styles.link}>Kurs erstellen</Link>
                    )}
                    <Link to="/profile" className={styles.link}>Mein Profil</Link>
                </div>
            </div>

            <form onSubmit={handleSearchSubmit} className={styles.searchForm}>
                <input 
                    className={`form-input ${styles.searchInput}`}
                    placeholder="Nach Studenten oder Dozenten suchen..." 
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </form>

            <button onClick={logout} className={styles.logoutBtn}>
                Ausloggen
            </button>
        </nav>
    );
};