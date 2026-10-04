import { z } from "zod";

import { RegisterBody } from "../../types/schemas.zod";

export const registerSchema = RegisterBody.extend({
    email: z.email("Invalid email address"),
    emailCopy: z.string().min(1, "Please confirm your email address"),
}).refine( (data) => data.email === data.emailCopy, {
    message: "Email addresses do not match",
    path: ["emailCopy"]
});

export type RegisterFormData = z.infer<typeof registerSchema>;