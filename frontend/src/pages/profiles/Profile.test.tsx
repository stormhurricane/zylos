import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { Profile } from './Profile';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import * as AuthContext from '../../context/AuthContext'; 
import { globalHandlers } from '../../test/handlers';

const server = setupServer(...globalHandlers);

const mockNavigate = vi.fn();
let mockParamId = '123';

vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
        useParams: () => ({ id: mockParamId })
    };
});

describe('Profile Component (Integration mit MSW)', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
        vi.restoreAllMocks(); 
        mockParamId = '123';
    });
    afterAll(() => server.close());

    it('should render own profile with courses correctly', async () => {
        mockParamId = '123';
        
        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: '123' },
            login: vi.fn(),
            logout: vi.fn(),
            loading: false,
            isAuthenticated: true
        } as any);

        renderWithAuthAndRouter(<Profile />);

        // wait for profile data
        expect(await screen.findByText('Jojen Doe')).toBeInTheDocument();
        expect(screen.getByText('own@uni.de')).toBeInTheDocument();
        expect(screen.getByText('Musterstraße 1')).toBeInTheDocument();
        expect(screen.getByText(/Student \(Matrikelnr: 1000001\)/i)).toBeInTheDocument();

        expect(await screen.findByText('Software Engineering')).toBeInTheDocument();
        expect(screen.getByText('Meine Kurse')).toBeInTheDocument();
    });

    it('should render other profile without private address and courses', async () => {
        mockParamId = '999'; 

        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: '123' }, // Login is 123, call is to 999
            login: vi.fn(),
            logout: vi.fn(),
            loading: false,
            isAuthenticated: true
        } as any);

        server.use(
            http.get('*/users/999', () => {
                return HttpResponse.json({
                    id: '999', 
                    firstName: 'Max',
                    lastName: 'Mustermann',
                    email: 'stranger@uni.de',
                    chair: 'Software Engineering',
                    researchArea: 'KI & Ethik',
                    privateAddress: 'Geheime Strasse 5'
                });
            })
        );

        renderWithAuthAndRouter(<Profile />);
        
        expect(await screen.findByText('Max Mustermann')).toBeInTheDocument();
        expect(screen.getByText('stranger@uni.de')).toBeInTheDocument();
        expect(screen.getByText('Software Engineering')).toBeInTheDocument();

        expect(screen.queryByText('Meine Kurse')).not.toBeInTheDocument();   
        expect(screen.queryByText('Geheime Strasse 5')).not.toBeInTheDocument();
    });

    it('should show error message when API fails', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
        
        server.use(
            http.get('*/users/*', () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderWithAuthAndRouter(<Profile />);

        expect(await screen.findByText(/Serverfehler|fehlgeschlagen/i)).toBeInTheDocument();

        consoleSpy.mockRestore();
    });

    it('should show a message if profile does not exist (404)', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
        mockParamId = '404';

        server.use(
            http.get('*/users/404', () => {
                return new HttpResponse(null, { status: 404 });
            })
        );

        renderWithAuthAndRouter(<Profile />);

        expect(await screen.findByText(/nicht gefunden|Serverfehler/i)).toBeInTheDocument();

        consoleSpy.mockRestore();
    });
});