import { screen, render } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi } from 'vitest';
import { ProtectedRoute } from './ProtectedRoute';
import * as AuthModule from '../context/AuthContext';
import { MemoryRouter, Routes, Route } from 'react-router-dom';

describe('ProtectedRoute Component', () => {

    it('should render PageLoader when auth is loading', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: null, loading: true, isInstructor: false, logout: vi.fn(), isAuthenticated: false
        } as any);

        render(
            <MemoryRouter initialEntries={['/secret']}>
                <Routes>
                    <Route path="/secret" element={
                        <ProtectedRoute>
                            <div>Geheimer Inhalt</div>
                        </ProtectedRoute>
                    } />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByRole('status')).toBeInTheDocument();
    });

    it('should redirect to login when no user is authenticated', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: null, loading: false, isInstructor: false, logout: vi.fn(), isAuthenticated: false
        } as any);

        render(
            <MemoryRouter initialEntries={['/secret']}>
                <Routes>
                    <Route path="/secret" element={
                        <ProtectedRoute>
                            <div data-testid="secret">Geheimer Inhalt</div>
                        </ProtectedRoute>
                    } />
                    <Route path="/login" element={<div data-testid="login-page">Login Seite</div>} />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.queryByTestId('secret')).not.toBeInTheDocument();
        expect(screen.getByTestId('login-page')).toBeInTheDocument();
    });

    it('should redirect to dashboard if user lacks the required role', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: { userId: 2, firstName: 'Student', lastName: 'X', role: 'STUDENT' },
            loading: false,
            isInstructor: false,
            logout: vi.fn(),
            isAuthenticated: true
        } as any);

        render(
            <MemoryRouter initialEntries={['/teacher-only']}>
                <Routes>
                    <Route path="/teacher-only" element={
                        <ProtectedRoute requiredRole="TEACHER">
                            <div data-testid="teacher-content">Dozenten Dashboard</div>
                        </ProtectedRoute>
                    } />
                    <Route path="/dashboard" element={<div data-testid="dashboard-page">Dashboard</div>} />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.queryByTestId('teacher-content')).not.toBeInTheDocument();
        expect(screen.getByTestId('dashboard-page')).toBeInTheDocument();
    });

    it('should render children if user has the correct role', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: { userId: 1, firstName: 'Prof', lastName: 'X', role: 'TEACHER' },
            loading: false,
            isInstructor: true,
            logout: vi.fn(),
            isAuthenticated: true
        } as any);

        render(
            <MemoryRouter initialEntries={['/teacher-only']}>
                <Routes>
                    <Route path="/teacher-only" element={
                        <ProtectedRoute requiredRole="TEACHER">
                            <div data-testid="teacher-content">Dozenten Dashboard</div>
                        </ProtectedRoute>
                    } />
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByTestId('teacher-content')).toBeInTheDocument();
    });
});