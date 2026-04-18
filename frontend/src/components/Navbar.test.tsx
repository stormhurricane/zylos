import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi } from 'vitest';
import { Navbar } from './Navbar';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext';

// Mock für useNavigate
const mockedUsedNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual as any,
        useNavigate: () => mockedUsedNavigate,
    };
});

describe('Navbar Component', () => {
    it('should render the brand name Zylos', () => {
        render(
            <BrowserRouter>
                <AuthProvider>
                    <Navbar />
                </AuthProvider>
            </BrowserRouter>
        );
        expect(screen.getByText('Zylos')).toBeInTheDocument();
    });

    it('should update search input on change', () => {
        render(
            <BrowserRouter>
                <AuthProvider>
                    <Navbar />
                </AuthProvider>
            </BrowserRouter>
        );
        const input = screen.getByPlaceholderText(/Nach Studenten oder Dozenten suchen/i) as HTMLInputElement;
        fireEvent.change(input, { target: { value: 'Mustermann' } });
        expect(input.value).toBe('Mustermann');
    });
});