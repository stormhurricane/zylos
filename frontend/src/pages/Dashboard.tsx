import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../api/axios';
import { Navbar } from '../components/Navbar';

const DEFAULT_AVATAR = "https://ui-avatars.com/api/?background=4CAF50&color=fff&name=";

export const Dashboard = () => {
    const { user } = useAuth();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const [searchResults, setSearchResults] = useState<any[]>([]);

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
    }, [searchParams]);

    return (
        <div style={{ minHeight: '100vh', backgroundColor: 'var(--color-light)' }}>
            <Navbar onSearch={performSearch} />
            
            <main style={{ flex: 1, padding: '40px' }}>
                <header style={{ marginBottom: '40px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h1>Willkommen, {user?.firstName}!</h1>
                </header>

                {/* Suchergebnisse */}
                <section>
                    {searchResults.length > 0 ? (
                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px' }}>
                            {searchResults.map((res: any, idx: number) => (
                                <div key={res.id || idx} className="auth-card" style={{ padding: '20px', textAlign: 'center', maxWidth: '100%' }}>
                                    <img 
                                        src={res.profilePicture || `${DEFAULT_AVATAR}${res.firstName}+${res.lastName}`} 
                                        alt="Avatar"
                                        style={{ width: '60px', height: '60px', borderRadius: '50%', marginBottom: '10px', objectFit: 'cover' }} 
                                    />
                                    <h4 style={{ margin: '5px 0' }}>{res.firstName} {res.lastName}</h4>
                                    <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{res.studySubject || res.chair || 'Nutzer'}</p>
                                    <button 
                                        onClick={() => navigate(`/profile/${res.id}`)}
                                        style={{ marginTop: '10px', background: 'none', border: '1px solid var(--color-primary)', color: 'var(--color-primary)', padding: '5px 10px', borderRadius: '5px', cursor: 'pointer' }}
                                    >
                                        Profil ansehen
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="auth-card" style={{ maxWidth: '100%', textAlign: 'center', color: 'var(--text-muted)' }}>
                            Nutze die Suche oben, um Kommilitonen oder Dozenten zu finden.
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
};
