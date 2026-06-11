import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { Login } from './Login';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import { userApi } from '../../api/userApi';
import { tokenService } from '../../utils/tokenService';

vi.mock('../../api/userApi', () => ({
    userApi: {
        login: vi.fn(),
    },
}));

vi.mock('../../utils/tokenService', () => ({
    tokenService: {
        getToken: vi.fn(() => null), 
        setToken: vi.fn(),
        clearToken: vi.fn(),
    },
}));

// Mock for useNavigate, to check Redirects
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
        useLocation: () => ({ state: { from: { pathname: '/dashboard' } } }),
    };
});

const renderLogin = async () => {
    const router = createBrowserRouter(
        [
            {
                path: '*',
                element: <Login />,
            },
        ],
    );

    const renderResult = render(
        <AuthProvider>
            <RouterProvider router={router} future={{ v7_startTransition: true }} />
        </AuthProvider>
    );

    await waitFor(() => {
        expect(screen.getByRole('heading', { name: /Willkommen zurück/i })).toBeInTheDocument();
    });

    return renderResult;
};

describe('Login Component (Integration)', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        localStorage.clear();
    });

    it('should show field errors when submitting empty form', async () => {
        await renderLogin();
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText('Bitte gib deine E-Mail oder Matrikelnummer ein.')).toBeInTheDocument();
        expect(await screen.findByText('Bitte gib dein Passwort ein.')).toBeInTheDocument();
    });

    it('should allow typing into identifier field', async () => {
        await renderLogin();

        const input = screen.getByPlaceholderText('z.B. 1000001') as HTMLInputElement;
        fireEvent.change(input, { target: { value: 'test@uni.de' } });
        expect(input.value).toBe('test@uni.de');
    });

    it('should display an error message on failed login', async () => {
        // Disable console for this test to prevent clutter from expected error logs
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

        vi.mocked(userApi.login).mockRejectedValueOnce({
            response: { data: { error: 'Ungültige Zugangsdaten' } }
        });

        await renderLogin();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'wrong@user.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'wrongpassword' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText('Ungültige Zugangsdaten')).toBeInTheDocument();

        consoleSpy.mockRestore();
    });

    it('should show loading state and redirect on success', async () => {
        vi.mocked(userApi.login).mockResolvedValueOnce({
            data: {
                accessToken: 'mocked-jwt-token',
                id: '1',
                email: 'user@test.de',
                role: 'STUDENT'
            }
        } as any);
        
        await renderLogin();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'user@test.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'password123' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        // Wait for the button to be enabled again, which indicates that the login process (including state updates) has completed
        await waitFor(() => {
            expect(screen.queryByText('Wird angemeldet...')).not.toBeInTheDocument();
        });

        // Afterwards we check for side effects (Routing, Token)
        expect(tokenService.setToken).toHaveBeenCalledWith('mocked-jwt-token');
        expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
    });
});