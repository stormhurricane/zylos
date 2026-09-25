import type { RegisterFormData } from "../../types/auth";

export const registerUser = async (formData: RegisterFormData) => {
    console.log(formData);
    console.log("Registering user...");
};