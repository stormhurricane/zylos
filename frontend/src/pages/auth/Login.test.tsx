import { screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { Login } from './Login';
import { sessionService } from  '../../utils/sessionService';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { setupServer } from 'msw/node';
import { globalHandlers, TEST_BASE_URL } from '../../test/handlers';
import { http, HttpResponse } from 'msw';

const server = setupServer(...globalHandlers);

vi.mock('../../utils/sessionService', () => ({
    sessionService: {
        saveSession: vi.fn(), 
        getSavedUser: vi.fn(),
        clearSession: vi.fn(),
        getToken: vi.fn(() => 'mocked-jwt-token'),
    },
}));

const renderLoginWithRoutes = (initialEntries = ['/login']) => {
    return renderWithAuthAndRouter(
        <>
            <Login />
            <div data-testid="dashboard-page">Dashboard Ansicht</div>
        </>,
        initialEntries
    );
};

describe('Login Component Integration', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
    });
    
    afterAll(() => server.close());

    it('should show field errors when submitting empty form', async () => {
        renderLoginWithRoutes();
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText('Bitte gib deine E-Mail oder Matrikelnummer ein.')).toBeInTheDocument();
        expect(await screen.findByText('Bitte gib dein Passwort ein.')).toBeInTheDocument();
    });

    it('should allow typing into identifier field', async () => {
        renderLoginWithRoutes();

        const input = screen.getByPlaceholderText('z.B. 1000001') as HTMLInputElement;
        fireEvent.change(input, { target: { value: 'test@uni.de' } });
        expect(input.value).toBe('test@uni.de');
    });

    it('should display an error message on failed login', async () => {
        server.use(
            http.post(`${TEST_BASE_URL}/users/login`, async () => {
                return HttpResponse.json({ message: 'Ungültige Zugangsdaten' }, { status: 401 });
            })
        );
       
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

        renderLoginWithRoutes();

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'wrong@user.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'wrongpassword' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByText(/Login fehlgeschlagen/i)).toBeInTheDocument();
        consoleSpy.mockRestore();
    });

    it('should show loading state and redirect to target route on success', async () => {
        server.use(
            http.post(`${TEST_BASE_URL}/users/login`, async () => {
                return HttpResponse.json({
                    accessToken: 'mocked-jwt-token',
                    userId: 1,
                    firstName: 'Max',
                    lastName: 'Mustermann',
                    role: 'STUDENT'
                }, { status: 200 });
            })
        );

        renderLoginWithRoutes([{ 
            pathname: '/login', 
            state: { from: { pathname: '/dashboard' } } 
        } as any]);

        fireEvent.change(screen.getByPlaceholderText('z.B. 1000001'), { target: { value: 'user@test.de' } });
        fireEvent.change(screen.getByPlaceholderText('••••••••'), { target: { value: 'password123' } });
        
        const loginButton = screen.getByRole('button', { name: /Anmelden/i });
        fireEvent.click(loginButton);

        expect(await screen.findByTestId('dashboard-page')).toBeInTheDocument();

        expect(sessionService.saveSession).toHaveBeenCalledWith(
            'mocked-jwt-token',
            expect.objectContaining({ userId: 1, role: 'STUDENT' })
        );
    });
});