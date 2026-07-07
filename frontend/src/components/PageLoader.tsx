import React from 'react';
import styles from './PageLoader.module.css';

export interface PageLoaderProps {
    message?: string;
}

export const PageLoader: React.FC<PageLoaderProps> = ({ message = 'Lädt Seite...' }) => (
    <div 
        className={styles.loaderContainer} 
        role="status" 
        aria-live="polite"
        aria-label={message}
    >
        <div className={styles.spinner}></div>
        <span className={styles.message}>{message}</span>
    </div>
);
