import { axiosClient } from "../../api/axios";
import { type RegisterRequest } from "../../types/index";

export const registerUser = async (formData: RegisterRequest): Promise<void> => {
    console.log(formData);
    await axiosClient.post("/v1/auth/register", formData);
};