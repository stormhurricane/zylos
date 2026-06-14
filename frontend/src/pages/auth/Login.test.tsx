import { screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { Login } from './Login';
import { tokenService } from '../../utils/tokenService';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { setupServer } from 'msw/node';
import { globalHandlers } from '../../test/handlers';
import { http, HttpResponse } from 'msw';

const server = setupServer(...globalHandlers);

vi.mock('../../utils/tokenService', () => ({
    tokenService: {
        getToken: vi.fn(() => null), 
        setToken: vi.fn(),
        clearToken: vi.fn(),
    },
}));

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
    const renderResult = renderWithAuthAndRouter(<Login />);
    await waitFor(() => {
        expect(screen.getByRole('heading', { name: /Willkommen zurück/i })).toBeInTheDocument();
    });
    return renderResult;
};

describe('Login Component Integration', () => {
    // handle MSW server lifecycle in Vitest
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
        localStorage.clear();
    });
    afterAll(() => server.close());

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
        server.use(
            http.post('*/users/login', async ({ request }) => {
                const body = (await request.json()) as any;
                    return new HttpResponse({ error: 'Ungültige Zugangsdaten' }, { status: 401 });
            })
        );
       
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

        await renderLogin();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'wrong@user.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'wrongpassword' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText(/Login fehlgeschlagen/i)).toBeInTheDocument();
        consoleSpy.mockRestore();
    });

    it('should show loading state and redirect on success', async () => {
        await renderLogin();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'user@test.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'password123' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        await waitFor(() => {
            expect(screen.queryByText('Wird angemeldet...')).not.toBeInTheDocument();
        });

        expect(tokenService.setToken).toHaveBeenCalledWith('mocked-jwt-token');
        expect(mockNavigate).toHaveBeenCalledWith('/dashboard');
    });
});