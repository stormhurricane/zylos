export type UserRole = 'STUDENT' | 'TEACHER';

export interface AuthResponse {
    accessToken: string;
    userId: number;
    role: UserRole;
    firstName: string;
    lastName: string;
}

export interface ProfileResponse {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    privateAddress: string;
    profilePicture?: string;
    matriculationNumber?: string; // Nur für Studenten
    studySubject?: string;        // Nur für Studenten
    researchArea?: string;        // Nur für Lehrende
    chair?: string;               // Nur für Lehrende
}

export interface LoginRequest {
    identifier: string; // Email oder Matrikelnummer
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

export interface ProfileUpdateRequest {
    password?: string;
    privateAddress?: string;
    profilePicture?: string;
    // Rollenspezifische Felder weggelassen für Kürze
}

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