import { apiClient } from "../../api/setupClient";
import { type LoginRequest, type RegisterRequest, type UserResponse } from "../../types/index";

export const authApi = {
    register: async (formData: RegisterRequest): Promise<void> => {
        return apiClient<void>("v1/auth/register", {
            method: "POST",
            body: JSON.stringify(formData)
        });
    },

    login: async (credentials: LoginRequest): Promise<UserResponse> => {
        return apiClient<UserResponse>("v1/auth/login", {
            method: "POST",
            body: JSON.stringify(credentials)
        });
    },

    logout: async (): Promise<void> => {
        return apiClient<void>("v1/auth/logout", {
            method: "POST"
        });
    },

    getMe: async (): Promise<UserResponse> => {
        return apiClient<UserResponse>("v1/auth/me", {
            method: "GET"
        });
    }


}