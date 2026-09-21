import { Link } from "react-router-dom";
import { useRegister } from "../hooks/useRegister";

export const Register = () => {
    const { formData, handleChange, handleSubmit, errors } = useRegister();

    const inputClasses = 
    "w-full px-3 py-2 bg-surface text-content-primary placeholder:text-content-muted " +
    "border border-border-subtle rounded-brand shadow-sm " +
    "focus:outline-none focus:ring-2 focus:ring-brand-primary focus:border-brand-primary " +
    "transition-colors";

    const labelClasses = "block text-sm font-medium text-content-secondary mb-1";
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
                    <form className="space-y-4" onSubmit={handleSubmit}>
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label htmlFor="firstname" className={labelClasses}>
                                    First Name
                                </label>
                                <input 
                                    id="firstname" name="firstname" type="text" autoComplete="given-name" required onChange={handleChange} value={formData.firstname} className={inputClasses}
                                ></input>
                                {errors.firstname && 
                                    <p role="alert" className={errorClasses}>
                                        {errors.firstname}
                                    </p>
                                }
                            </div>
                            <div>
                                <label htmlFor="lastname" className={labelClasses}>
                                    Last Name
                                    </label>
                                <input 
                                    id="lastname" name="lastname" type="text" autoComplete="family-name" required onChange={handleChange} value={formData.lastname} className={inputClasses}
                                ></input>
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
                                <input 
                                    id="email" name="email" type="email" autoComplete="email" required onChange={handleChange} value={formData.email} className={inputClasses}
                                ></input>
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
                                <input 
                                    id="emailCopy" name="emailCopy" type="email" autoComplete="email" required onChange={handleChange} value={formData.emailCopy} className={inputClasses}
                                ></input>
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
                                <input 
                                    id="username" name="username" type="text" required onChange={handleChange} value={formData.username} className={inputClasses}
                                ></input>
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
                                <input 
                                    id="password" name="password" type="password" autoComplete="new-password" required minLength={8} onChange={handleChange} value={formData.password} className={inputClasses}
                                ></input>
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