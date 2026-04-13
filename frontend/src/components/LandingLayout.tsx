import React from 'react';

interface LandingLayoutProps {
    children: React.ReactNode;
}

export const LandingLayout: React.FC<LandingLayoutProps> = ({ children }) => {
    return (
        <div className="landing-container">
            <div className="landing-left">
                <img src="/src/assets/logo_round.webp" alt="Zylos Logo" style={{ width: '140px', marginBottom: '30px', filter: 'drop-shadow(0 10px 15px rgba(0,0,0,0.1))' }} />
                <h1 style={{ fontSize: '3rem', margin: 0 }}>Zylos</h1>
                <p style={{ fontSize: '1.25rem', opacity: 0.85, marginTop: '10px', fontWeight: '300', textAlign: 'center' }}>Dein Campus. Deine Projekte. Deine Zukunft.</p>
            </div>
            <div className="landing-right">
                {children}
            </div>
        </div>
    );
};
