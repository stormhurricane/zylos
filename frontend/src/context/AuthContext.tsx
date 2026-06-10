import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthResponse, LoginRequest } from '../api/types';
import { userApi } from '../api/userApi';
import { tokenService } from '../utils/tokenService';

// new type with everything from AuthResponse except 'accessToken' (which we handle separately for security)
type UserData = Omit<AuthResponse, 'accessToken'>;

interface AuthContextType {
    user: UserData | null;
    login: (credentials: LoginRequest) => Promise<void>;
    logout: () => void;
    isAuthenticated: boolean;
    loading: boolean;
    isAuthenticating: boolean;
    isInstructor: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<UserData | null>(null);
    const [loading, setLoading] = useState(true);
    const [isAuthenticating, setIsAuthenticating] = useState(false);

    useEffect(() => {
        const savedUserStr = localStorage.getItem('user');
        const token = tokenService.getToken(); 

        if (savedUserStr && token) {
            try {
                const parsedUser: UserData = JSON.parse(savedUserStr);
                setUser(parsedUser);
            } catch (error) {
                console.error('Fehler beim Parsen der Benutzersitzung:', error);
                tokenService.clearToken();
                localStorage.removeItem('user');
                setUser(null);
            }
        } else {
            tokenService.clearToken();
            localStorage.removeItem('user');
            setUser(null);
        }
        
        setLoading(false);
    }, []);

    const login = async (credentials: LoginRequest) => {
        setIsAuthenticating(true);
        try {
            const response = await userApi.login(credentials);
            const { accessToken, ...userData } = response.data;
            
            tokenService.setToken(accessToken);
            localStorage.setItem('user', JSON.stringify(userData));
            
            // first local auth process must be fully completed BEFORE changing user state and triggering routing!
            setIsAuthenticating(false); 
            setUser(userData); 
        } catch (error) {
            // reset in error case
            setIsAuthenticating(false);
            throw error;
        }
    };

    const logout = () => {
        setIsAuthenticating(true);
        tokenService.clearToken();
        localStorage.removeItem('user');
        setUser(null);
        setIsAuthenticating(false);
    };

    // Compute derived state for easy access in components
    return (
        <AuthContext.Provider value={{ 
            user, 
            login, 
            logout, 
            isAuthenticated: !!user,
            loading,
            isAuthenticating,
            isInstructor: user?.role === 'TEACHER'
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
