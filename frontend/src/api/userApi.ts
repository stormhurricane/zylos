import api from './axios';
import axios from 'axios';
import { 
    AuthResponse, 
    LoginRequest, 
    ProfileResponse, 
    ProfileUpdateRequest, 
    StudentRegistrationRequest,
    TeacherRegistrationRequest 
} from './types';

export const userApi = {
    /** Performs user login */
    login: (credentials: LoginRequest) => 
        api.post<AuthResponse>('/users/login', credentials),

    /** Fetches a profile (own or specific ID) */
    getProfile: async (id?: string): Promise<ProfileResponse | null> => {
        try {
            const endpoint = id ? `/users/${id}` : '/users/me';
            const response = await api.get<ProfileResponse>(endpoint);
            return response.data;
        } catch (error) {
            // If 404, User does not exist - return null to indicate "not found"
            if (axios.isAxiosError(error) && error.response?.status === 404) {
                return null; 
            }
            // every other error (500, network down) we re-throw so that the hook can handle it in the catch block
            throw error; 
        }
    },

    /** Updates profile data */
    updateProfile: (data: ProfileUpdateRequest) => 
        api.put<ProfileResponse>('/users/me', data),

    /** Searches for users by name or other criteria */
    searchUsers: (query: string) =>
        api.get<ProfileResponse[]>('/users/search', { params: { q: query } }),

    /** Registers a new student */
    registerStudent: (data: StudentRegistrationRequest) =>
        api.post<void>('/users/register/student', data),

    /** Registers a new teacher */
    registerTeacher: (data: TeacherRegistrationRequest) =>
        api.post<void>('/users/register/teacher', data),
};