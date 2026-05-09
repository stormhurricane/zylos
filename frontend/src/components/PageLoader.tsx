import React from 'react';

interface PageLoaderProps {
    message?: string;
}

export const PageLoader: React.FC<PageLoaderProps> = ({ message = 'Lädt Seite...' }) => (
    <div style={{ padding: '2rem', textAlign: 'center', color: 'var(--text-muted)' }}>
        {/* Hier könnte später eine globale Spinner-Komponente ergänzt werden */}
        {message}
    </div>
);
