import { screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { act } from 'react';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { Register } from './Register';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { globalHandlers } from '../../test/handlers';

const server = setupServer(...globalHandlers);

import { Routes, Route } from 'react-router-dom';

const renderRegisterWithRoutes = (initialEntries = ['/register']) => {
    return renderWithAuthAndRouter(
        <Routes>
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<div data-testid="login-page">Login Ansicht</div>} />
        </Routes>,
        initialEntries
    );
};

describe('Register Component Integration', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
        vi.useRealTimers();
    });
    
    afterAll(() => server.close());

    it('should toggle user type and render dynamic input fields accordingly', async () => {
        renderRegisterWithRoutes();

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
        renderRegisterWithRoutes();

        fireEvent.change(screen.getByLabelText(/Vorname/i), { target: { value: 'Max' } });
        fireEvent.change(screen.getByLabelText(/Nachname/i), { target: { value: 'Mustermann' } });
        fireEvent.change(screen.getByLabelText(/E-Mail Adresse/i), { target: { value: 'konflikt@uni.de' } });
        fireEvent.change(screen.getByLabelText(/Passwort/i), { target: { value: 'Pass1234!' } });
        fireEvent.change(screen.getByLabelText(/Studienfach/i), { target: { value: 'Informatik' } });

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
        vi.useFakeTimers();
        renderRegisterWithRoutes();

        fireEvent.change(screen.getByLabelText(/Vorname/i), { target: { value: 'Max' } });
        fireEvent.change(screen.getByLabelText(/Nachname/i), { target: { value: 'Mustermann' } });
        fireEvent.change(screen.getByLabelText(/E-Mail Adresse/i), { target: { value: 'neu@uni.de' } });
        fireEvent.change(screen.getByLabelText(/Passwort/i), { target: { value: 'Pass1234!' } });
        fireEvent.change(screen.getByLabelText(/Studienfach/i), { target: { value: 'Informatik' } });

        const submitButton = screen.getByRole('button', { name: /Jetzt registrieren/i });
        fireEvent.click(submitButton);

        await act(async () => {
            await vi.advanceTimersByTimeAsync(0);
        });

        expect(screen.getByText(/Registrierung erfolgreich!/i)).toBeInTheDocument();
        expect(screen.queryByTestId('login-page')).not.toBeInTheDocument();

        await act(async () => {
            await vi.advanceTimersByTimeAsync(3000);
        });

        expect(screen.getByTestId('login-page')).toBeInTheDocument();
    });
});