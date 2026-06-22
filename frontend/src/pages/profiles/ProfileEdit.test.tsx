import { screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { describe, it, expect, vi, beforeAll, afterEach, afterAll } from 'vitest';
import { ProfileEdit } from './ProfileEdit';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { globalHandlers, TEST_BASE_URL } from '../../test/handlers';
import { act } from 'react';

const server = setupServer(...globalHandlers);

describe('ProfileEdit Component Integration', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => {
        server.resetHandlers();
        vi.clearAllMocks();
    });
    afterAll(() => server.close());

    // Testfall 1: Loading State (Nativ über MSW verzögert)
    it('should render the ProfileEdit component with loading state', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/users/me`, async () => {
                await new Promise(resolve => setTimeout(resolve, 100));
                return HttpResponse.json({ id: 123 });
            })
        );

        renderWithAuthAndRouter(<ProfileEdit />, ['/profile/edit'], '/profile/edit');
        expect(screen.getByText('Einstellungen werden geladen...')).toBeInTheDocument();
    });

    it('should display the specific fields for a student', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/users/me`, () => {
                return HttpResponse.json({
                    id: 123,
                    firstName: 'Max',
                    lastName: 'Mustermann',
                    matriculationNumber: '1000001', 
                    studySubject: 'Informatik'
                });
            })
        );

        renderWithAuthAndRouter(<ProfileEdit />, ['/profile/edit'], '/profile/edit');

        const input = await screen.findByDisplayValue('Informatik');
        expect(input).toBeInTheDocument();
        
        expect(screen.getByText('Studienfach')).toBeInTheDocument();
        expect(screen.queryByText('Lehrstuhl')).not.toBeInTheDocument();
    });

    it('should display the specific fields for a lecturer/employee', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/users/me`, () => {
                return HttpResponse.json({
                    id: 123,
                    firstName: 'Dr.',
                    lastName: 'Schmidt',
                    matriculationNumber: null,
                    chair: 'Praktische Informatik',
                    researchArea: 'KI'
                });
            })
        );

        renderWithAuthAndRouter(<ProfileEdit />, ['/profile/edit'], '/profile/edit');

        await screen.findByDisplayValue('Praktische Informatik');

        expect(screen.getByText('Lehrstuhl')).toBeInTheDocument();
        expect(screen.getByText('Forschungsgebiet')).toBeInTheDocument();
        expect(screen.queryByText('Studienfach')).not.toBeInTheDocument();
    });

    it('should display an error message when the hook returns an error', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/users/me`, () => {
                return HttpResponse.json({ id: 123 });
            }),
            http.put(`${TEST_BASE_URL}/users/me`, () => {
                return new HttpResponse(null, { status: 400 });
            })
        );

        renderWithAuthAndRouter(<ProfileEdit />, ['/profile/edit'], '/profile/edit');

        const submitButton = await screen.findByRole('button', { name: /änderungen speichern/i });
        await act(async () => {
            await userEvent.click(submitButton);
        });

        const errorField = await screen.findByText('Update fehlgeschlagen.');
        expect(errorField).toBeInTheDocument();
        expect(errorField).toHaveClass('error-message');
    });

    it('should forward submits and cancels to the hook', async () => {
        let putPayload: any = null;
        
        server.use(
            http.get(`${TEST_BASE_URL}/users/me`, () => {
                return HttpResponse.json({ id: 123, firstName: 'Max', privateAddress: 'Musterweg 5' });
            }),
            http.put(`${TEST_BASE_URL}/users/me`, async ({ request }) => {
                putPayload = await request.json();
                return HttpResponse.json({ id: 123 });
            })
        );

        renderWithAuthAndRouter(<ProfileEdit />, ['/profile/edit'], '*');

        const submitButton = await screen.findByRole('button', { name: /änderungen speichern/i });
        
        await act(async () => { await userEvent.click(submitButton) });

        await waitFor(() => {
            expect(putPayload).not.toBeNull();
        });
        expect(putPayload.privateAddress).toBe('Musterweg 5');
    });
});