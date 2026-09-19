export interface RegisterFormData {
    firstname: string;
    lastname: string;
    email: string;
    emailCopy: string;
    username: string;
    password: string;
}

export type RegisterFormErrors = Partial<Record<keyof RegisterFormData, string>>;

export interface LoginFormData {
    username: string;
    password: string;
}

