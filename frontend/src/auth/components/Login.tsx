import { zodResolver } from '@hookform/resolvers/zod';
import { useMutation } from '@tanstack/react-query';
import { toast } from "sonner";
import { useForm } from 'react-hook-form';
import { Link, useNavigate } from 'react-router-dom';
import { type LoginFormData, LoginSchema } from '../schemas/login.schema';
import { type ErrorResponse } from '../../types';
import { authApi } from '../api/authApi';

export const Login =()  => {
	const navigate = useNavigate();

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
	
	const { mutate, isPending, isError, error } = useMutation<
		unknown,
		ErrorResponse,
		LoginFormData
	>({
		mutationFn: authApi.login,
		onSuccess: async () => {
			toast.success("Login successful");

			if(REDIRECT_DELAY > 0){
				await new Promise((resolve) => setTimeout(resolve, REDIRECT_DELAY));
			}

			navigate("/dashboard");
		},
		onError: (error: ErrorResponse) => {

		}
	});

	const onSubmit = (data: LoginFormData) => {
		mutate(data);
	}


    return ( 
      <div >
		<div>
			<h2>
				Login
			</h2>
		</div>

		<div>
			<div>
				<form onSubmit={handleSubmit(onSubmit)} noValidate>
					{isError && (
						<div role="alert">
							<p>{error.message || "An error occured during login"}</p>
						</div>
					)} 

					<div>
						<label htmlFor="identifier">
							Identifier
						</label>
						<input 
							id="identifier" 
							type="text"
							{...register("identifier")}
						/>
					</div>
					
					<div >
						<label htmlFor="password">
							Password
						</label>
						<input 
							type="password"
							id="password"
							{...register("password")}
						/>
					</div>
					
					<div>
						<button 
							type="submit"
							disabled={isPending}
						>
							Login
						</button>
					</div>
				</form>

				<p>
					Don't have an account?{" "}
					<Link to="/register">
						Register Now
					</Link>
				</p>
			</div>
			
		</div>
      </div>
    );
};
