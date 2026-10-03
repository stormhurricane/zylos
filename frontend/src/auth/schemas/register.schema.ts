import { z } from "zod";

import { RegisterBody } from "../../types/schemas.zod";

export const registerSchema = RegisterBody.extend({
    email: z.email("Invalid email address"),
    emailCopy: z.string().min(1, "Please confirm your email address"),
}).refine( (data) => data.email === data.emailCopy, {
    message: "Email addresses do not match",
    path: ["emailCopy"]
});

export const registerSchema2 = z.object({
    firstName: z.string().min(1, "First name is required"),
    lastName: z.string().min(1, "Last name is required"),
    email: z.email("Invalid email address"),
    password: z.string().min(8, "Password must be at least 8 characters long"),
    username: z.string().min(3, "Username must be at least 3 characters long"),
    emailCopy: z.string().min(1, "Please confirm your email address"),
    role: z.enum(["STUDENT", "TEACHER"], "Role must be either STUDENT or TEACHER"),
}).refine( (data) => data.email === data.emailCopy, {
    message: "Email addresses do not match",
    path: ["emailCopy"]
});

export type RegisterFormData = z.infer<typeof registerSchema>;