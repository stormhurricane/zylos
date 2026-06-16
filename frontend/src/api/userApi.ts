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
    /** Executes the user login */
    login: (credentials: LoginRequest): Promise<AuthResponse> => 
        api.post('/users/login', credentials),

    /** Gets profile(own or ID) - or null at 404 */
    getProfile: async (id?: number): Promise<ProfileResponse | null> => {
        try {
            const endpoint = id ? `/users/${id}` : '/users/me';
            const response = await api.get<any, ProfileResponse>(endpoint);
            return response;
        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status === 404) {
                return null; 
            }
            throw error; 
        }
    },

    /** Updates Profile data */
    updateProfile: (data: ProfileUpdateRequest): Promise<ProfileResponse> => 
        api.put('/users/me', data),

    /** Searches for users */
    searchUsers: (query: string): Promise<ProfileResponse[]> =>
        api.get('/users/search', { params: { q: query } }),

    /** Registers a new student */
    registerStudent: (data: StudentRegistrationRequest): Promise<void> =>
        api.post('/users/register/student', data),

    /** Registers a new teacher */
    registerTeacher: (data: TeacherRegistrationRequest): Promise<void> =>
        api.post('/users/register/teacher', data),
};