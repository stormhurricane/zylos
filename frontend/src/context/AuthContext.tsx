import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthResponse, LoginRequest } from '../api/types';
import { userApi } from '../api/userApi';

interface AuthContextType {
    user: AuthResponse | null;
    login: (credentials: LoginRequest) => Promise<void>;
    logout: () => void;
    isAuthenticated: boolean;
    loading: boolean;
    isInstructor: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<AuthResponse | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const savedUser = localStorage.getItem('user');
        const token = localStorage.getItem('accessToken');
        if (savedUser && token) {
            setUser(JSON.parse(savedUser));
        }
        setLoading(false);
    }, []);

    const login = async (credentials: LoginRequest) => {
        try {
            const response = await userApi.login(credentials);
            const authData = response.data;
            
            localStorage.setItem('accessToken', authData.accessToken);
            localStorage.setItem('user', JSON.stringify(authData));
            setUser(authData);
        } catch (error) {
            console.error('Login failed', error);
            throw error;
        }
    };

    const logout = () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('user');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ 
            user, 
            login, 
            logout, 
            isAuthenticated: !!user,
            loading,
            isInstructor: user ? !('matriculationNumber' in user) : false
        }}>
            {!loading && children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
