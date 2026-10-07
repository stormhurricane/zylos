import { apiClient } from "../../api/setupClient";
import { type RegisterRequest } from "../../types/index";

export const registerUser = async (formData: RegisterRequest): Promise<void> => {
    console.log(formData);
    return apiClient<void>('v1/auth/register', {
        method: 'POST',
        body: JSON.stringify(formData)
    });
};