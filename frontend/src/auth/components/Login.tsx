import { zodResolver } from '@hookform/resolvers/zod';
import { QueryClient, useMutation, useQueryClient } from '@tanstack/react-query';
import { toast } from "sonner";
import { useForm } from 'react-hook-form';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import { type LoginFormData, LoginSchema } from '../schemas/login.schema';
import { type ErrorResponse } from '../../types';
import { useAuth } from '../context/AuthContext';
import { authApi } from '../api/authApi';

export const Login =()  => {
	const navigate = useNavigate();
	const location = useLocation();
	const queryClient = useQueryClient();

	const { user, login } = useAuth();

	const { register, handleSubmit, setError, formState: { errors} } = useForm<LoginFormData>({
		resolver: zodResolver(LoginSchema),
		mode: "onBlur",
		defaultValues: {
			identifier: "",
			password: ""
		}
	});
	
	const isTest = import.meta.env.MODE === 'test';
    const REDIRECT_DELAY = isTest ? 0 : 2000;
	
	const from = (location.state as { from?: { pathname: string }})?.from?.pathname || "/dashboard"; 
	
	const { mutate, isPending, isError, error } = useMutation<
		unknown,
		ErrorResponse,
		LoginFormData
	>({
		mutationFn: login,
		onSuccess: async (userData) => {
			toast.success("Login successful");

			navigate(from, { replace: true });
		},
		onError: (error: ErrorResponse) => {

		}
	});

	const onSubmit = (data: LoginFormData) => {
		mutate(data);
	}

	if(user){
		return <Navigate to="/dashboard" replace />;
	}

	const inputClasses =
        "w-full px-3 py-2 bg-surface text-content-primary placeholder:text-content-muted " +
        "border border-border-subtle rounded-brand shadow-sm " +
        "focus:outline-none focus:ring-2 focus:ring-brand-primary focus:border-brand-primary " +
        "transition-colors aria-[invalid=true]:border-status-error";

	const labelClasses = "block text-sm font-medium text-content-secondary mb-1";
    const errorClasses = "mt-1 text-sm text-status-error";

    return ( 
      <div className="min-h-screen bg-surface-app flex flex-col justify-center py-12 px-4 sm:px-6 lg:px-8">
            <div className="sm:mx-auto sm:w-full sm:max-w-md">
                <h2 className="mt-6 text-center text-3xl font-extrabold text-content-primary">
                    Welcome back
                </h2>
            </div>

			<div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
                <div className="bg-surface py-8 px-4 shadow-card sm:rounded-brand sm:px-10">
                    <form className="space-y-4" onSubmit={handleSubmit(onSubmit)} noValidate>
					
					{isError && (
						<div role="alert" className="p-3 bg-status-error/10 border border-status-error/20 rounded text-sm text-status-error">
							<p>{error?.message || "Invalid identifier or password."}</p>
						</div>
					)}

					<div>
						<label htmlFor="identifier" className={labelClasses}>
								Identifier
						</label>
						<input 
							id="identifier" 
							type="text"
							autoComplete='username'
							aria-invalid={!!errors.identifier}
							aria-describedby={errors.identifier ? 'identifier-error' : undefined}
							className={inputClasses}
							{...register("identifier")}
						/>

						{errors.identifier && (
							<p id="identifier-error" role="alert" className={errorClasses}>
								{errors.identifier.message}
							</p>
						)}
					</div>
					
					<div >
						<label htmlFor="password" className={labelClasses}>
							Password
						</label>
						<input 
							id="password"
							type="password"
							autoComplete='current-password'
							aria-invalid={!!errors.password}
							aria-describedby={errors.password ? 'password-error' : undefined}
							className={inputClasses}
							{...register("password")}
						/>

						{errors.password && (
							<p id="password-error" role="alert" className={errorClasses}>
								{errors.password.message}
							</p>
						)}
					</div>
					
					<div className="pt-2">
						<button 
							type="submit"
							disabled={isPending}
							className="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-brand shadow-sm text-sm font-medium text-white bg-brand-primary hover:opacity-90 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-primary transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
						>
							{isPending ? "Logging in..." : "Login"}
						</button>
					</div>
				</form>

				<p className="mt-6 text-center text-sm text-content-secondary">
					Don't have an account?{" "}
					<Link to="/register" className="font-medium text-brand-primary hover:underline">
						Register Now
					</Link>
				</p>
			</div>
			
		</div>
      </div>
    );
};
