import { type RegisterRequest } from "../../types/index";

export const registerUser = async (formData: RegisterRequest) => {
    console.log(formData);
    console.log("Registering user...");
};