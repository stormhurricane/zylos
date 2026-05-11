import React, { useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import styles from './Navbar.module.css';

export const Navbar: React.FC = () => {
    // Get authentication state and methods from context
    const { user, logout, isInstructor } = useAuth();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchSubmit = (e: React.FormEvent) => {
        // Redirect to dashboard with the search query as a URL parameter
        e.preventDefault();
        navigate(`/dashboard?q=${encodeURIComponent(searchQuery)}`);
    };

    // Helper function to handle active class styling with CSS Modules
    const getNavLinkClass = ({ isActive }: { isActive: boolean }) => 
        isActive ? `${styles.link} ${styles.active}` : styles.link;

    return (
        <nav className={styles.nav}>
            <div className={styles.linksContainer}>
                <Link to="/dashboard" style={{ textDecoration: 'none' }}>
                    {/* Application Brand/Logo */}
                    <h2 className={styles.brand}>Zylos</h2>
                </Link>
                
                <div className={styles.links}>
                    <NavLink to="/courses" className={getNavLinkClass} end>
                        Alle Kurse
                    </NavLink>
                    {/* Only show 'Create Course' for instructors */}
                    {isInstructor && (
                        <NavLink to="/courses/new" className={getNavLinkClass} end>
                            Kurs erstellen
                        </NavLink>
                    )}
                    <NavLink to="/profile" className={getNavLinkClass} end>
                        Mein Profil
                    </NavLink>
                </div>
            </div>

            <form onSubmit={handleSearchSubmit} className={styles.searchForm}>
                <input 
                    className={`form-input ${styles.searchInput}`}
                    placeholder="Nach Studenten oder Dozenten suchen..." 
                    aria-label="Search students or instructors"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </form>

            <button onClick={logout} className={styles.logoutBtn}>
                Logout
            </button>
        </nav>
    );
};