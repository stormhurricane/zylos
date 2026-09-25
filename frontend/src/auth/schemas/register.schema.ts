import { z } from "zod";

export const registerSchema = z.object({
    firstname: z.string().min(1, "First name is required"),
    lastname: z.string().min(1, "Last name is required"),
    email: z.email("Invalid email address"),
    password: z.string().min(8, "Password must be at least 8 characters long"),
    username: z.string().min(3, "Username must be at least 3 characters long"),
    emailCopy: z.string().min(1, "Please confirm your email address")
}).refine( (data) => data.email === data.emailCopy, {
    message: "Email addresses do not match",
    path: ["emailCopy"]
});

export type RegisterFormData = z.infer<typeof registerSchema>;