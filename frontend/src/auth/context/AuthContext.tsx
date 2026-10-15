import { type LoginRequest, type UserResponse } from "../../types";
import { createContext, useState, useEffect, useContext, type ReactNode } from "react";
import { authApi } from "../api/authApi";

interface AuthContextType {
    user: UserResponse | null,
    isLoading: boolean,
    login: (credentials: LoginRequest) => Promise<void>,
    logout: () => Promise<void>
};

interface AuthProviderProps {
    children: ReactNode
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({children} : AuthProviderProps) => {
    const [user, setUser] = useState<UserResponse | null>(null);

    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const initializeAuth = async () => {
            try {
                const userData = await authApi.getMe();
                setUser(userData);
            } catch {
                setUser(null);
            } finally {
                setIsLoading(false);
            }
        };

        initializeAuth();
    }, []);

    const login = async (credentials: LoginRequest) => {
        const userData = await authApi.login(credentials);
        setUser(userData);
    };

    const logout = async () => {
        try {
            await authApi.logout();
        } finally {
            setUser(null);
        }
    };

    return (
        <AuthContext.Provider value = {{user, isLoading, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = (): AuthContextType => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used inside of AuthProvider");
    }
    return context;
}