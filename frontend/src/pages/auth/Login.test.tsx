import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi } from 'vitest';
import { Login } from './Login';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';

// Mocken des API-Moduls
vi.mock('../api/axios', () => ({
    default: {
        post: vi.fn(),
    },
}));

describe('Login Component', () => {
    it('should show field errors when submitting empty form', async () => {
        render(
            <BrowserRouter>
                <AuthProvider>
                    <Login />
                </AuthProvider>
            </BrowserRouter>
        );

        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText('Bitte gib deine E-Mail oder Matrikelnummer ein.')).toBeInTheDocument();
        expect(await screen.findByText('Bitte gib dein Passwort ein.')).toBeInTheDocument();
    });

    it('should allow typing into identifier field', () => {
        render(
            <BrowserRouter>
                <AuthProvider>
                    <Login />
                </AuthProvider>
            </BrowserRouter>
        );

        const input = screen.getByPlaceholderText('z.B. 1000001') as HTMLInputElement;
        fireEvent.change(input, { target: { value: 'test@uni.de' } });
        expect(input.value).toBe('test@uni.de');
    });
});