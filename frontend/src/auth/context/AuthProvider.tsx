import { type LoginRequest, type UserResponse } from "../../types";
import { useState, useEffect, type ReactNode } from "react";
import { authApi } from "../api/authApi";
import { toast } from "sonner";
import { AuthContext } from "./AuthContext";

interface AuthProviderProps {
    children: ReactNode
}


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
        toast.success("Login successful");
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
