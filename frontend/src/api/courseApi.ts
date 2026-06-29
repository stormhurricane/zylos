import api from './axios';
import { Course, ParticipantsResponse, Material } from './types';

export const courseApi = {
    /** Gets all available courses */
    getAllCourses: (): Promise<Course[]> => 
        api.get('/courses'),
    
    /** Gets details for a specific course via ID */
    getCourseById: (id: number): Promise<Course> => 
        api.get(`/courses/${id}`),

    /** Creates a new Course (Only teachers) */
    createCourse: (data: Omit<Course, 'id'>): Promise<Course> => 
        api.post(`/courses`, data),
    
    /** Gets all courses, in which current user is enrolled */
    getMyCourses: (): Promise<Course[]> => 
        api.get(`/courses/my-enrollments`),

    /** Imports courses via csv */
    importCsv: (file: File): Promise<Course[]> => {
        const formData = new FormData();
        formData.append('file', file);
        return api.post(`/courses/import`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    /** Enrolled current user into course */
    enroll: (courseId: number): Promise<void> => 
        api.post(`/courses/${courseId}/enroll`),

    /** Gets list of participants of a course */
    getParticipants: (courseId: number): Promise<ParticipantsResponse> => 
        api.get(`/courses/${courseId}/participants`),

    /** Adds a user to the course */
    addParticipant: (courseId: number, userId: number, role: 'STUDENT' | 'INSTRUCTOR'): Promise<void> =>
        api.post(`/courses/${courseId}/participants`, { userId, role }),

    /** Gets all material from a course */
    getMaterials: (courseId: number): Promise<Material[]> => 
        api.get(`/courses/${courseId}/materials`),

    /** Uploads material to a course */
    uploadMaterial: (courseId: number, title: string, file: File): Promise<Material> => {
        const formData = new FormData();
        formData.append('title', title);
        formData.append('file', file);
        return api.post(`/courses/${courseId}/materials`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },

    /** Downloads material as a blob */
    downloadMaterial: (materialId: number): Promise<Blob> => 
        api.get(`/courses/materials/${materialId}/download`, {
            responseType: 'blob'
        })
};