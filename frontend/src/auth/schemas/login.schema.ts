import { z } from "zod";

import { LoginBody } from "../../types/schemas.zod";

export const LoginSchema = LoginBody.extend({
    identifier: z.string().min(2, ""),
    password: z.string().min(6)
});

export type LoginFormData = z.infer<typeof LoginSchema>;