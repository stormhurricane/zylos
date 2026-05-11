import React from 'react';
import styles from './LandingLayout.module.css';
import logo from '../assets/logo_round.webp'; 

interface LandingLayoutProps {
    children: React.ReactNode;
}

export const LandingLayout: React.FC<LandingLayoutProps> = ({ children }) => {
    return (
        <main className={styles.container}> 
            <section className={styles.left}> 
                <img src={logo} alt="Zylos Logo" className={styles.logo} /> 
                <h1 className={styles.title}>Zylos</h1>
                <p className={styles.subtitle}>Dein Campus. Deine Projekte. Deine Zukunft.</p>
            </section>
            <section className={styles.right}> 
                {children}
            </section>
        </main>
    );
};
