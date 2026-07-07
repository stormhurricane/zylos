import axios, { InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import { sessionService } from '../utils/sessionService';

declare module 'axios' {
    export interface AxiosInstance {
        request<T = any, R = T>(config: AxiosRequestConfig): Promise<R>;
        get<T = any, R = T>(url: string, config?: AxiosRequestConfig): Promise<R>;
        delete<T = any, R = T>(url: string, config?: AxiosRequestConfig): Promise<R>;
        head<T = any, R = T>(url: string, config?: AxiosRequestConfig): Promise<R>;
        options<T = any, R = T>(url: string, config?: AxiosRequestConfig): Promise<R>;
        post<T = any, R = T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<R>;
        put<T = any, R = T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<R>;
        patch<T = any, R = T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<R>;
    }
}

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

// Request Interceptor for JWT
api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
    const token = sessionService.getToken();
    if (token && config.headers) {
        config.headers.set('Authorization', `Bearer ${token}`);
    }
    return config;
});

// Response Interceptor: extract data globally and catch errors
api.interceptors.response.use(
    (response: AxiosResponse) => {
        return Promise.resolve(response.data);    },
    (error) => {
        if (error.response?.status === 401) {
            sessionService.clearSession();
            // trigger global event
            window.dispatchEvent(new Event('auth-unauthorized'));
        }
        return Promise.reject(error);
    }
);

export default api;