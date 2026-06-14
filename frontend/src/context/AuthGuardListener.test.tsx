import { render } from '@testing-library/react';
import { AuthGuardListener } from './AuthGuardListener';
import * as reactRouterDom from 'react-router-dom';

// Wir nutzen das Referenz-Gesetz für Drittbibliotheken, um die Navigation zu überwachen
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual<typeof reactRouterDom>('react-router-dom');
    return {
        ...actual,
        useNavigate: vi.fn(),
    };
});

describe('AuthGuardListener', () => {
    it('sollte bei Erhalt des auth-unauthorized Events auf /login weiterleiten', () => {
        const mockNavigate = vi.fn();
        vi.spyOn(reactRouterDom, 'useNavigate').mockReturnValue(mockNavigate);

        render(<AuthGuardListener />);

        window.dispatchEvent(new Event('auth-unauthorized'));

        expect(mockNavigate).toHaveBeenCalledWith('/login', {
            state: { error: 'Session abgelaufen. Bitte neu anmelden.' },
        });
    });
});

