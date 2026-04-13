import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const Navbar = ({ onSearch }: { onSearch?: (query: string) => void }) => {
    const { logout } = useAuth();
    const navigate = useNavigate();
    const [q, setQ] = useState('');

    const handleSearchSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (onSearch) {
            onSearch(q);
        } else {
            // Falls wir nicht auf dem Dashboard sind, leiten wir mit Query-Parameter hin
            navigate(`/dashboard?q=${q}`);
        }
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
                <h2 style={{ color: 'var(--color-primary)', margin: 0, cursor: 'pointer' }} onClick={() => navigate('/dashboard')}>Zylos</h2>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                    <span onClick={() => navigate('/profile')} style={{ cursor: 'pointer', fontWeight: 500 }}>Mein Profil</span>
                </div>
            </div>

            <form onSubmit={handleSearchSubmit} style={{ flex: 1, maxWidth: '500px', margin: '0 40px' }}>
                <input 
                    className="form-input" 
                    style={{ background: 'rgba(255,255,255,0.1)', border: 'none', color: 'white', padding: '10px 20px' }}
                    placeholder="Nach Studenten oder Dozenten suchen..." 
                    value={q}
                    onChange={(e) => setQ(e.target.value)}
                />
            </form>

            <button 
                onClick={logout}
                style={{ 
                    background: 'transparent', 
                    border: '1px solid #ff6b6b', 
                    color: '#ff6b6b', 
                    padding: '8px 15px', 
                    borderRadius: '6px', 
                    cursor: 'pointer' 
                }}
            >
                Ausloggen
            </button>
        </nav>
    );
};