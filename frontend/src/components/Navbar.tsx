import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface NavbarProps {
    onSearch?: (query: string) => void;
}

export const Navbar: React.FC<NavbarProps> = ({ onSearch }) => {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (onSearch) {
            onSearch(searchQuery);
        } else {
            navigate(`/dashboard?q=${encodeURIComponent(searchQuery)}`);
        }
    };

    // Lehrende haben keine Matrikelnummer, Studierende hingegen schon.
    // Diese Logik folgt der Implementierung in Profile.tsx.
    const isInstructor = user && !('matriculationNumber' in user);

    const linkStyle: React.CSSProperties = {
        cursor: 'pointer',
        fontWeight: 500,
        color: 'white',
        textDecoration: 'none'
    };

    return (
        <nav style={{
            height: '70px',
            background: 'var(--color-dark)',
            display: 'flex',
            alignItems: 'center',
            padding: '0 40px',
            justifyContent: 'space-between',
            color: 'white',
            position: 'sticky',
            top: 0,
            zIndex: 1000
        }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '30px' }}>
                <Link to="/dashboard" style={{ textDecoration: 'none' }}>
                    <h2 style={{ color: 'var(--color-primary)', margin: 0, cursor: 'pointer' }}>Zylos</h2>
                </Link>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                    <Link to="/courses" style={linkStyle}>Alle Kurse</Link>
                    {isInstructor && (
                        <Link to="/courses/new" style={linkStyle}>Kurs erstellen</Link>
                    )}
                    <Link to="/profile" style={linkStyle}>Mein Profil</Link>
                </div>
            </div>

            <form onSubmit={handleSearchSubmit} style={{ flex: 1, maxWidth: '500px', margin: '0 40px' }}>
                <input 
                    className="form-input" 
                    style={{ background: 'rgba(255,255,255,0.1)', border: 'none', color: 'white', padding: '10px 20px', width: '100%', borderRadius: '6px' }}
                    placeholder="Nach Studenten oder Dozenten suchen..." 
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </form>

            <button 
                onClick={logout}
                style={{ background: 'transparent', border: '1px solid #ff6b6b', color: '#ff6b6b', padding: '8px 15px', borderRadius: '6px', cursor: 'pointer' }}
            >
                Ausloggen
            </button>
        </nav>
    );
};