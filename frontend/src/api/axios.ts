import axios, { InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import { tokenService } from '../utils/tokenService';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request Interceptor for JWT
api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const token = tokenService.getToken();
    if (token && config.headers) {
        config.headers.set('Authorization', `Bearer ${token}`);
    }
    return config;
});

// Response Interceptor for Global Error Handling
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Token expired or unauthorized, force logout
            localStorage.removeItem('accessToken');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;