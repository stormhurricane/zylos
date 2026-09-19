import { useState } from "react";
import type { RegisterFormData, RegisterFormErrors } from "../../../types/auth";

export const useRegister = () => {
    const [formData, setFormData] = useState<RegisterFormData>({
        firstname: "",
        lastname: "",
        email: "",
        emailCopy: "",
        username: "",
        password: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value
        }));
    };

    const errors: RegisterFormErrors = {};

    if(formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
        errors.email = "Please enter a valid Email address.";
    }
    if(formData.email && formData.emailCopy && formData.email !== formData.emailCopy) {
        errors.emailCopy = "Email addresses do not match.";
    }
    if(formData.password && formData.password.length < 8) {
        errors.password = "Password must be at least 8 characters long.";
    }
    if(formData.username && formData.username.length < 3) {
        errors.username = "Username must be at least 3 characters long.";
    }
    if(formData.firstname && formData.firstname.length < 2) {
        errors.firstname = "First name must be at least 2 characters long.";
    }
    if(formData.lastname && formData.lastname.length < 2) {
        errors.lastname = "Last name must be at least 2 characters long.";
    }

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (Object.keys(errors).length > 0) return;
        
        // Execute API registration call here
    };

    return {
        formData,
        handleChange,
        handleSubmit,
        errors
    };
};