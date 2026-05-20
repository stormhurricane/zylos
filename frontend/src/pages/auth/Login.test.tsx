import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { Login } from './Login';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import axios from '../../api/axios';

// Mock the API module
vi.mock('../../api/axios', () => ({
    default: {
        post: vi.fn(),
    },
}));

// Helper to render the component with all necessary providers
const renderLogin = () => {
    return render(
        <BrowserRouter>
            <AuthProvider>
                <Login />
            </AuthProvider>
        </BrowserRouter>
    );
};

describe('Login Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should show field errors when submitting empty form', async () => {
        renderLogin();
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText('Bitte gib deine E-Mail oder Matrikelnummer ein.')).toBeInTheDocument();
        expect(await screen.findByText('Bitte gib dein Passwort ein.')).toBeInTheDocument();
    });

    it('should allow typing into identifier field', () => {
        renderLogin();

        const input = screen.getByPlaceholderText('z.B. 1000001') as HTMLInputElement;
        fireEvent.change(input, { target: { value: 'test@uni.de' } });
        expect(input.value).toBe('test@uni.de');
    });

    it('should display an error message on failed login', async () => {
        // Setup mock for failure
        vi.mocked(axios.post).mockRejectedValueOnce({
            response: { data: { error: 'Ungültige Zugangsdaten' } }
        });

        renderLogin();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'wrong@user.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'wrongpassword' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        // Wait for the async error message from the mocked API to appear
        expect(await screen.findByText('Ungültige Zugangsdaten')).toBeInTheDocument();
    });

    it('should show loading state during submission', async () => {
        // Mock a slow response
        vi.mocked(axios.post).mockReturnValueOnce(new Promise(() => {}));
        
        renderLogin();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'user@test.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'password123' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        // The text should change to the loading indicator defined in Login.tsx
        expect(screen.getByText('Wird angemeldet...')).toBeInTheDocument();
        expect(loginButton).toBeDisabled();
    });
});