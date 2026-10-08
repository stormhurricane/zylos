import { type components } from "./api.types";

export type RegisterRequest = components['schemas']['RegisterRequest'];

export const UserRole = {
  STUDENT: 'STUDENT',
  TEACHER: 'TEACHER',
} satisfies Record<string, RegisterRequest['role']>;

export type UserRole = (typeof UserRole)[keyof typeof UserRole];


export interface LoginFormData {
    username: string;
    password: string;
}

