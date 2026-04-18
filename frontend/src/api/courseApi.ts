import api from './axios';

const API_URL = ''; // Base URL is already handled in axios.ts

export interface Course {
    id: number;
    title: string;
    type: 'LECTURE' | 'SEMINAR';
    term: 'SUMMER' | 'WINTER';
    academicYear: string;
}

export interface UserResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
}

export interface ParticipantsResponse {
    instructors: UserResponse[];
    students: UserResponse[];
}

export interface Material {
    id: number;
    title: string;
    fileName: string;
    contentType: string;
}

export const courseApi = {
    getAllCourses: () => api.get<Course[]>(`/courses`),
    
    getCourseById: (id: number) => api.get<Course>(`/courses/${id}`),

    createCourse: (data: Omit<Course, 'id'>) => 
        api.post<Course>(`/courses`, data),
    
    getMyCourses: () => api.get<Course[]>(`/courses/my-enrollments`),

    importCsv: (file: File) => {
        const formData = new FormData();
        formData.append('file', file);
        return api.post<Course[]>(`/courses/import`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    enroll: (courseId: number) => 
        api.post(`/courses/${courseId}/enroll`),

    getParticipants: (courseId: number) => 
        api.get<ParticipantsResponse>(`/courses/${courseId}/participants`),

    getMaterials: (courseId: number) => 
        api.get<Material[]>(`/courses/${courseId}/materials`),

    uploadMaterial: (courseId: number, title: string, file: File) => {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('file', file);
        return api.post<Material>(`/courses/${courseId}/materials`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    downloadMaterial: (materialId: number) => 
        api.get(`/courses/materials/${materialId}/download`, {
            responseType: 'blob'
        })
};