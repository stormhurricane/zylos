import { Link, NavLink } from 'react-router-dom';
import logo from '../../assets/logo_round.webp'; 
import { useAuth } from '../../auth/hooks/useAuth';

export const Header = () => {
    // const { user, logout } = useAuth();
    return (
        <header className="sticky top-0 z-40 w-full border-b border-border-subtle bg-surface/80 backdrop-blur-md">
            <div className="flex h-16 items-center justify-between px-6 max-w-7xl mx-auto">                
                
                {/* Logo + Branding */}
                <div className="flex items-center gap-8">
                    <Link to="/home" className="flex items-center gap-3">
                        <img src={logo} alt="Zylos Logo" className="h-9 w-auto" />
                        <span className="text-xl font-bold text-content-primary">Zylos</span>
                    </Link>
                </div>

                {/* Main Navigation */}
                <nav className="hidden md:flex items-center gap-6">
                    <NavLink 
                        to="/home" 
                        className={({ isActive }) => 
                            `text-sm font-medium transition-colors ${
                                isActive ? 'text-brand-primary font-semibold' : 'text-content-secondary hover:text-content-primary'
                            }`
                        }
                    >
                        Dashboard
                    </NavLink>

                    
                </nav>  
            </div>
        </header>
    );
}