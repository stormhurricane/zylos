import React from 'react';
import { Link, NavLink } from 'react-router-dom';
import { useNavbar } from './useNavbar';
import styles from './Navbar.module.css';

export const Navbar: React.FC = () => {
    
    const { 
        isInstructor, 
        searchQuery, 
        setSearchQuery, 
        handleSearchSubmit, 
        logout 
    } = useNavbar();

    const getNavLinkClass = ({ isActive }: { isActive: boolean }) => 
        isActive ? `${styles.link} ${styles.active}` : styles.link;

    return (
        <nav className={styles.nav}>
            <div className={styles.linksContainer}>
                <Link to="/dashboard" style={{ textDecoration: 'none' }}>
                    <h2 className={styles.brand}>Zylos</h2>
                </Link>
                
                <div className={styles.links}>
                    <NavLink to="/courses" className={getNavLinkClass} end>
                        Alle Kurse
                    </NavLink>
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