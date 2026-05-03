import React from 'react';
import styles from './LandingLayout.module.css';

interface LandingLayoutProps {
    children: React.ReactNode;
}

export const LandingLayout: React.FC<LandingLayoutProps> = ({ children }) => {
    return (
        <div className={styles.container}>
            <div className={styles.left}>
                <img src="/src/assets/logo_round.webp" alt="Zylos Logo" className={styles.logo} />
                <h1 className={styles.title}>Zylos</h1>
                <p className={styles.subtitle}>Dein Campus. Deine Projekte. Deine Zukunft.</p>
            </div>
            <div className={styles.right}>
                {children}
            </div>
        </div>
    );
};
