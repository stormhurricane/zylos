import { screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { act } from 'react';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { Register } from './Register';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { globalHandlers } from '../../test/handlers';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

const server = setupServer(...globalHandlers);

describe('Register Component ', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
        vi.useRealTimers();
    });
    afterAll(() => server.close());

    it('should toggle user type and render dynamic input fields accordingly', async () => {
        renderWithAuthAndRouter(<Register />);

        expect(screen.getByLabelText(/Studienfach/i)).toBeInTheDocument();
        expect(screen.queryByLabelText(/Lehrstuhl/i)).not.toBeInTheDocument();

        const teacherButton = screen.getByRole('button', { name: /Lehrender/i });
        fireEvent.click(teacherButton);

        expect(screen.getByLabelText(/Lehrstuhl/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Forschungsgebiet/i)).toBeInTheDocument();
        expect(screen.queryByLabelText(/Studienfach/i)).not.toBeInTheDocument();
    });

    it('should display API error message when registration fails', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});
        renderWithAuthAndRouter(<Register />);

        fireEvent.change(screen.getByLabelText(/Vorname/i), { target: { value: 'Max' } });
        fireEvent.change(screen.getByLabelText(/Nachname/i), { target: { value: 'Mustermann' } });
        fireEvent.change(screen.getByLabelText(/E-Mail Adresse/i), { target: { value: 'konflikt@uni.de' } });
        fireEvent.change(screen.getByLabelText(/Passwort/i), { target: { value: 'Pass1234!' } });
        fireEvent.change(screen.getByLabelText(/Studienfach/i), { target: { value: 'Informatik' } });

        // ovveride the register to create error
        server.use(
            http.post('*/users/register/student', () => {
                return HttpResponse.json(
                    { error: 'E-Mail Adresse bereits vergeben.' }, 
                    { status: 409 }
                );
            })
        );

        const submitButton = screen.getByRole('button', { name: /Jetzt registrieren/i });
        fireEvent.click(submitButton);

        expect(await screen.findByText(/bereits vergeben/i)).toBeInTheDocument();
        
        consoleSpy.mockRestore();
    });

    it('should show success card on successful registration and redirect after 3 seconds', async () => {
        // start fake timer before
        vi.useFakeTimers();

        renderWithAuthAndRouter(<Register />);

        fireEvent.change(screen.getByLabelText(/Vorname/i), { target: { value: 'Max' } });
        fireEvent.change(screen.getByLabelText(/Nachname/i), { target: { value: 'Mustermann' } });
        fireEvent.change(screen.getByLabelText(/E-Mail Adresse/i), { target: { value: 'neu@uni.de' } });
        fireEvent.change(screen.getByLabelText(/Passwort/i), { target: { value: 'Pass1234!' } });
        fireEvent.change(screen.getByLabelText(/Studienfach/i), { target: { value: 'Informatik' } });

        const submitButton = screen.getByRole('button', { name: /Jetzt registrieren/i });
        fireEvent.click(submitButton);

        // as time is frozen, let async events be resolved
        await act(async () => {
            await vi.advanceTimersByTimeAsync(0);
        });

        expect(screen.getByText(/Registrierung erfolgreich!/i)).toBeInTheDocument();
        expect(mockNavigate).not.toHaveBeenCalled();

        // trigger set timetout
        await act(async () => {
            await vi.advanceTimersByTimeAsync(3000);
        });

        // Verifizieren, dass der Redirect geklappt hat
        expect(mockNavigate).toHaveBeenCalledWith('/login');
    });
});