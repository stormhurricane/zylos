import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const useNavbar = () => {
    const { logout, isInstructor } = useAuth();
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (searchQuery.trim()) {
            navigate(`/dashboard?q=${encodeURIComponent(searchQuery.trim())}`);
        }
    };

    return {
        isInstructor,
        searchQuery,
        setSearchQuery,
        handleSearchSubmit,
        logout
    };
};