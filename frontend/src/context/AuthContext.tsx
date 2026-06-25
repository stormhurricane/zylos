import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { AuthResponse, LoginRequest } from '../api/types';
import { userApi } from '../api/userApi';
import { sessionService } from '../utils/sessionService';

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
        const savedUser = sessionService.getSavedUser();
        const token = sessionService.getToken();

        if (savedUser && token) {
            setUser(savedUser);
        } else {
            sessionService.clearSession();
            setUser(null);
        }
        setLoading(false);
    }, []);

    useEffect(() => {
        const handleUnauthorized = () => {
            setUser(null); 
        };

        window.addEventListener('auth-unauthorized', handleUnauthorized);
        return () => {
            window.removeEventListener('auth-unauthorized', handleUnauthorized);
        };
    }, []);

    const login = async (credentials: LoginRequest) => {
        setIsAuthenticating(true);
        try {
            const response = await userApi.login(credentials);
            const { accessToken, ...userData } = response;
            
            sessionService.saveSession(accessToken, userData);
            
            setIsAuthenticating(false); 
            setUser(userData); 
        } catch (error) {
            setIsAuthenticating(false);
            throw error;
        }
    };

    const logout = () => {
        setIsAuthenticating(true);
        sessionService.clearSession(); 
        setUser(null);
        setIsAuthenticating(false);
    };

    return (
        <AuthContext.Provider value={{ 
            user, 
            login, 
            logout, 
            isAuthenticated: !!user,
            loading,
            isAuthenticating,
            isInstructor: user?.role === 'INSTRUCTOR'
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