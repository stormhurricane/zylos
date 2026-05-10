import api from './axios';
import { 
    AuthResponse, 
    LoginRequest, 
    ProfileResponse, 
    ProfileUpdateRequest, 
    StudentRegistrationRequest 
} from './types';

export const userApi = {
    /** Performs user login */
    login: (credentials: LoginRequest) => 
        api.post<AuthResponse>('/auth/login', credentials),

    /** Fetches a profile (own or specific ID) */
    getProfile: (id?: string) => 
        api.get<ProfileResponse>(id ? `/users/${id}` : '/users/me'),

    /** Updates profile data */
    updateProfile: (data: ProfileUpdateRequest) => 
        api.put<ProfileResponse>('/users/me', data),

    /** Searches for users by name or other criteria */
    searchUsers: (query: string) =>
        api.get<ProfileResponse[]>('/users/search', { params: { q: query } }),
};