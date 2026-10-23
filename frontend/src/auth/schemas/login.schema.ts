import { z } from "zod";

import { LoginBody } from "../../types/schemas.zod";

export const LoginSchema = LoginBody.extend({
    identifier: z.string().trim().min(1, "Please enter your username or email"),
    password: z.string().min(6, "Passwords must be at least 6 characters long")
});

export type LoginFormData = z.infer<typeof LoginSchema>;