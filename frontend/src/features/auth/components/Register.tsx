import { useState } from "react";
import { Link } from "react-router-dom";
import type { RegisterFormData, RegisterFormErrors } from "../../../types/auth";

export const Register = () => {
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

    const inputClasses = "w-full px-3 py-2 border border-border-subtle rounded-brand shadow-sm focus:outline-none focus:ring-2 focus:ring-brand-primary focus:border-brand-primary text-slate-900 placeholder-slate-400 transition-colors";
    const labelClasses = "block text-sm font-medium text-slate-700 mb-1";
    const errorClasses = "mt-1 text-sm text-status-error";

    return (
        <div className="min-h-screen bg-surface-app flex flex-col justify-center py-12 sm:px-6 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <h2 className="mt-6 text-center text-3xl font-extrabold text-slate-900">
                    Create an account
                </h2>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                <div className="bg-surface py-8 px-4 shadow-card sm:rounded-brand sm:px-10">
                    <form className="space-y-4">
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="firstName" className={labelClasses}>
                                    First Name
                                </label>
                                <input id="firstname" name="firstname" type="text" autoComplete="given-name" required onChange={handleChange} value={formData.firstname} className={inputClasses}></input>
                                {errors.firstname && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.firstname}
                                    </p>
                                }
                            </div>
                            <div>
                                <label htmlFor="lastName" className={labelClasses}>
                                    Last Name
                                    </label>
                                <input id="lastName" name="lastName" type="text" autoComplete="family-name" required onChange={handleChange} className={inputClasses}></input>
                                {errors.lastname && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.lastname}
                                    </p>
                                }
                            </div>
                        </div>
                        
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="email" className={labelClasses}>
                                    Email
                                </label>
                                <input id="email" name="email" type="email" autoComplete="email" required onChange={handleChange} className={inputClasses}></input>
                                {errors.email && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.email}
                                    </p>
                                }
                            </div>
                            <div>
                                <label htmlFor="emailCopy" className={labelClasses}>
                                    Repeat Email
                                </label>
                                <input id="emailCopy" name="emailCopy" type="email" autoComplete="email" required onChange={handleChange} className={inputClasses}></input>
                                {errors.emailCopy && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.emailCopy}
                                    </p>
                                }
                            </div>
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="username" className={labelClasses}>
                                    Username
                                </label>
                                <input id="username" name="username" type="text" required onChange={handleChange} className={inputClasses}></input>
                                {errors.username && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.username}
                                    </p>
                                }
                            </div>
                            <div>
                                <label htmlFor="password" className={labelClasses}>
                                    Password
                                </label>
                                <input id="password" name="password" type="password" autoComplete="new-password" required minLength={8} onChange={handleChange} className={inputClasses}></input>
                                {errors.password && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.password}
                                    </p>
                                }
                            </div>
                        </div>

                        <div className="pt-2">
                            <button 
                                type="submit"
                                className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-brand shadow-sm text-sm font-medium text-white bg-brand-primary hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-primary transition-colors cursor-pointer"
                            >
                                Register
                            </button>
                        </div>
                    </form>

                    <p className="mt-6 text-center text-sm text-slate-500">
                        Already have an account?{" "}
                        <Link to="/login" className="font-medium text-brand-primary hover:underline">
                            Login
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
};