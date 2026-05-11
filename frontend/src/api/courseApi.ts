import api from './axios';
import { Course, ParticipantsResponse, Material } from './types';

export const courseApi = {
    /** Fetches all available courses */
    getAllCourses: () => api.get<Course[]>(`/courses`),
    
    /** Fetches details for a specific course by ID */
    getCourseById: (id: number) => api.get<Course>(`/courses/${id}`),

    /** Creates a new course (Teacher only) */
    createCourse: (data: Omit<Course, 'id'>) => 
        api.post<Course>(`/courses`, data),
    
    /** Fetches courses where the current user is enrolled */
    getMyCourses: () => api.get<Course[]>(`/courses/my-enrollments`),

    /** Imports courses via CSV file */
    importCsv: (file: File) => {
        const formData = new FormData();
        formData.append('file', file);
        return api.post<Course[]>(`/courses/import`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' } // Browser sets boundary automatically
        });
    },

    /** Enrolls the current user into a course */
    enroll: (courseId: number) => 
        api.post<void>(`/courses/${courseId}/enroll`),

    /** Fetches the list of instructors and students for a course */
    getParticipants: (courseId: number) => 
        api.get<ParticipantsResponse>(`/courses/${courseId}/participants`),

    /** Adds a specific user as a participant to a course (Instructor only) */
    addParticipant: (courseId: number, userId: number) =>
        api.post<void>(`/courses/${courseId}/participants`, { userId }),

    /** Fetches all teaching materials for a course */
    getMaterials: (courseId: number) => 
        api.get<Material[]>(`/courses/${courseId}/materials`),

    /** Uploads a file as teaching material for a course */
    uploadMaterial: (courseId: number, title: string, file: File) => {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('file', file);
        return api.post<Material>(`/courses/${courseId}/materials`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    /** Downloads a material file as a Blob */
    downloadMaterial: (materialId: number) => 
        api.get(`/courses/materials/${materialId}/download`, {
            responseType: 'blob'
        })
};