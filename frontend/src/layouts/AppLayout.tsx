import { Outlet } from "react-router-dom";
import { Header } from "./components/Header";

export const AppLayout = () => {
    return (
        <div className="min-h-screen flex flex-col bg-bg-app text-text-primary">
            <Header/>

            <main className="flex-1 w-full max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">                
                <Outlet/>
            </main>

            <footer className="w-full border-t border-border-subtle bg-bg-surface/50 py-4">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center text-xs text-text-muted">
                    © {new Date().getFullYear()} Zylos. All rights reserved.
                </div>
            </footer>
        </div>
    );
};