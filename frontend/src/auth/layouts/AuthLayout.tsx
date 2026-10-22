import { Outlet } from "react-router-dom";
import logo from '../../assets/logo_round.webp'; 

export const AuthLayout = () => {

    return (
        <div className="min-h-screen grid grid-cols-1 lg:grid-cols-2 bg-surface-app">
            {/* Left Side: HERO */}
            <div className="hidden lg:flex flex-col justify-between p-12 bg-surface border-r border-border-subtle">
                <div className="flex items-center gap-3">
                    <img src={logo} alt="Zylos Logo" className="h-10 w-auto" />
                    <span className="text-xl font-bold text-content-primary">Zylos</span>
                </div>

                <div className="my-auto max-w-lg">
                    <h1 className="text-4xl font-extrabold text-content-primary tracking-tight">
                        Zylos
                    </h1>
                    <p className="mt-4 text-lg text-content-secondary">
                        Your Campus. Your Projects. Your Future.
                    </p>
                </div>

                <div className="text-sm text-content-muted">
                    © {new Date().getFullYear()} Zylos. All rights reserved.
                </div>
            </div>
        
            {/* Right Side: Form */}
            <div className="flex flex-col justify-center items-center p-6 sm:p-12 overflow-y-auto">
                <div className="lg:hidden flex items-center gap-3 mb-8">
                    <img src={logo} alt="Zylos Logo" className="h-8 w-auto" />
                    <span className="text-lg font-bold text-content-primary">Zylos</span>
                </div>

                <div className="w-full max-w-md">
                    <Outlet />
                </div>
            </div>
        </div>
    );
}