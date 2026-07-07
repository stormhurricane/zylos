import { act } from 'react';
import { screen, render, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi } from 'vitest';
import { AuthProvider, useAuth } from './AuthContext';
import { sessionService } from '../utils/sessionService';
import { userApi } from '../api/userApi';

vi.mock('../utils/sessionService', () => ({
    sessionService: {
        getSavedUser: vi.fn(),
        getToken: vi.fn(),
        saveSession: vi.fn(),
        clearSession: vi.fn(),
    },
}));

vi.mock('../api/userApi', () => ({
    userApi: {
        login: vi.fn(),
    },
}));

// dummy component for testing
const TestingComponent = () => {
    const { user, isAuthenticated, isInstructor, login, logout } = useAuth();
    return (
        <div>
            <div data-testid="auth-state">
                {isAuthenticated ? `Eingeloggt als ${user?.firstName}` : 'Nicht eingeloggt'}
            </div>
            <div data-testid="role-state">{isInstructor ? 'Dozent' : 'Kein Dozent'}</div>
            <button onClick={() => login({ identifier: 'test', password: '123' })}>Login Triggern</button>
            <button onClick={logout}>Logout Triggern</button>
        </div>
    );
};

describe('AuthContext & AuthProvider', () => {

    it('should initialize with user data if a valid session exists', () => {
        vi.mocked(sessionService.getSavedUser).mockReturnValue({
            userId: 1, firstName: 'Max', lastName: 'Mustermann', role: 'STUDENT'
        });
        vi.mocked(sessionService.getToken).mockReturnValue('valid-token');

        render(
            <AuthProvider>
                <TestingComponent />
            </AuthProvider>
        );

        expect(screen.getByTestId('auth-state')).toHaveTextContent('Eingeloggt als Max');
        expect(screen.getByTestId('role-state')).toHaveTextContent('Kein Dozent');
    });

    it('should initialize as unauthenticated if no session exists', () => {
        vi.mocked(sessionService.getSavedUser).mockReturnValue(null);
        vi.mocked(sessionService.getToken).mockReturnValue(null);

        render(
            <AuthProvider>
                <TestingComponent />
            </AuthProvider>
        );

        expect(screen.getByTestId('auth-state')).toHaveTextContent('Nicht eingeloggt');
        expect(sessionService.clearSession).toHaveBeenCalled(); 
    });

    it('should update state and save session on successful login', async () => {
        vi.mocked(sessionService.getSavedUser).mockReturnValue(null);
        vi.mocked(sessionService.getToken).mockReturnValue(null);
        
        vi.mocked(userApi.login).mockResolvedValue({
            accessToken: 'new-jwt-token',
            userId: 2,
            firstName: 'Prof',
            lastName: 'X',
            role: 'INSTRUCTOR'
        });

        render(
            <AuthProvider>
                <TestingComponent />
            </AuthProvider>
        );

        // push login
        act(() => {
            screen.getByRole('button', { name: /Login Triggern/i }).click();
        });

        await waitFor(() => {
            expect(sessionService.saveSession).toHaveBeenCalledWith(
                'new-jwt-token',
                { userId: 2, firstName: 'Prof', lastName: 'X', role: 'INSTRUCTOR' }
            );
        });

        expect(screen.getByTestId('auth-state')).toHaveTextContent('Eingeloggt als Prof');
        expect(screen.getByTestId('role-state')).toHaveTextContent('Dozent'); // Rolle korrekt abgeleitet!
    });

    it('should log out immediately when the global auth-unauthorized event fires', async () => {
        vi.mocked(sessionService.getSavedUser).mockReturnValue({
            userId: 1, firstName: 'Max', lastName: 'Mustermann', role: 'STUDENT'
        });
        vi.mocked(sessionService.getToken).mockReturnValue('valid-token');

        render(
            <AuthProvider>
                <TestingComponent />
            </AuthProvider>
        );

        expect(screen.getByTestId('auth-state')).toHaveTextContent('Eingeloggt als Max');

        act(() => {
            window.dispatchEvent(new Event('auth-unauthorized'));
        });

        expect(screen.getByTestId('auth-state')).toHaveTextContent('Nicht eingeloggt');
    });
});