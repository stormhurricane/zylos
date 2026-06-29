export type UserRole = 'STUDENT' | 'INSTRUCTOR';

export interface AuthResponse {
    accessToken: string;
    userId: number;
    role: UserRole;
    firstName: string;
    lastName: string;
}

export interface UserSearchResponse {
    id: number,
    firstName: string,
    lastName: string,
    profilePicture?: string,
    subInfo: string
}

export interface ProfileResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    privateAddress: string;
    profilePicture?: string;
    matriculationNumber?: string; // Only for students
    studySubject?: string;        // Only for students
    researchArea?: string;        // Only for instructors
    chair?: string;               // Only for instructors
}

export interface LoginRequest {
    identifier: string; // Email or matriculation number
    password: string;
}

export interface StudentRegistrationRequest {
    firstName: string;
    lastName: string;
    password: string;
    email: string;
    privateAddress: string;
    studySubject: string;
    profilePicture?: string;
}

export interface TeacherRegistrationRequest {
    firstName: string;
    lastName: string;
    password: string;
    email: string;
    privateAddress: string;
    chair: string;
    researchArea: string;
    profilePicture?: string;
}

export interface ProfileUpdateRequest {
    firstName?: string;
    lastName?: string;
    email?: string;
    password?: string;
    privateAddress?: string;
    profilePicture?: string;
    studySubject?: string;
    researchArea?: string;
    chair?: string;
}

export enum CourseType {
    LECTURE = 'LECTURE',
    SEMINAR = 'SEMINAR'
}

export enum SemesterTerm {
    SUMMER = 'SUMMER',
    WINTER = 'WINTER'
}

export interface Course {
    id: number;
    title: string;
    type: CourseType;
    term: SemesterTerm;
    academicYear: string;
}

export interface UserResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    role: 'STUDENT' | 'INSTRUCTOR';
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