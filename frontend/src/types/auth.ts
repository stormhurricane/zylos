export const UserRole = {
  STUDENT: 'STUDENT',
  TEACHER: 'TEACHER',
} as const;

export type UserRole = (typeof UserRole)[keyof typeof UserRole];

export interface RegisterFormData {
    firstname: string;
    lastname: string;
    email: string;
    emailCopy: string;
    username: string;
    password: string;
    role: UserRole;
}

export type RegisterFormErrors = Partial<Record<keyof RegisterFormData, string>>;

export interface LoginFormData {
    username: string;
    password: string;
}

