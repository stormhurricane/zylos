import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { Profile } from './Profile';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import * as AuthContext from '../../context/AuthContext'; 
import { globalHandlers, TEST_BASE_URL } from '../../test/handlers';

const server = setupServer(...globalHandlers);

describe('Profile Component (Integration mit MSW)', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
        vi.restoreAllMocks(); 
    });
    afterAll(() => server.close());

    it('should render own profile with courses correctly', async () => {
        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: 123 }, 
            loading: false,
            isAuthenticated: true
        } as any);

        renderWithAuthAndRouter(
            <Profile />,
            ['/profile/123'],  
            '/profile/:id'     
        );

        expect(await screen.findByText('Jojen Doe')).toBeInTheDocument();
        expect(screen.getByText('own@uni.de')).toBeInTheDocument();
        expect(screen.getByText('Musterstraße 1')).toBeInTheDocument();
        expect(screen.getByText(/Student/i)).toBeInTheDocument();

        expect(screen.getByText('Meine Kurse')).toBeInTheDocument();
    });

    it('should render other profile without private address and courses', async () => {
        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: 123 }, 
            loading: false,
            isAuthenticated: true
        } as any);

        server.use(
            http.get(`${TEST_BASE_URL}/users/999`, () => {
                return HttpResponse.json({
                    id: 999, 
                    firstName: 'Max',
                    lastName: 'Mustermann',
                    email: 'stranger@uni.de',
                    chair: 'Software Engineering',
                    researchArea: 'KI & Ethik',
                    privateAddress: 'Geheime Strasse 5'
                });
            })
        );

        renderWithAuthAndRouter(
            <Profile />,
            ['/profile/999'],
            '/profile/:id'
        );
        
        expect(await screen.findByText('Max Mustermann')).toBeInTheDocument();
        expect(screen.getByText('stranger@uni.de')).toBeInTheDocument();
        expect(screen.getByText('Software Engineering')).toBeInTheDocument();

        expect(screen.queryByText('Meine Kurse')).not.toBeInTheDocument();   
        expect(screen.queryByText('Geheime Strasse 5')).not.toBeInTheDocument();
    });

    it('should show error message when API fails', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
        
        server.use(
            http.get(`${TEST_BASE_URL}/users/500`, () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderWithAuthAndRouter(
            <Profile />,
            ['/profile/500'],
            '/profile/:id'
        );

        expect(await screen.findByText(/Serverfehler|fehlgeschlagen/i)).toBeInTheDocument();
        consoleSpy.mockRestore();
    });
});