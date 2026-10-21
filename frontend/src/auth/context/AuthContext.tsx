import { type LoginRequest, type UserResponse } from "../../types";
import { createContext } from "react";

export interface AuthContextType {
    user: UserResponse | null,
    isLoading: boolean,
    login: (credentials: LoginRequest) => Promise<void>,
    logout: () => Promise<void>
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);