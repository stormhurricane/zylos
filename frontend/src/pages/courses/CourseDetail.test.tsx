import { screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, beforeAll, afterEach, afterAll, vi } from 'vitest';
import { CourseDetail } from './CourseDetail';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { renderWithAuthAndRouter } from '../../test/testUtils'; // Pfad anpassen
import * as AuthContext from '../../context/AuthContext';
import { globalHandlers } from '../../test/handlers';



const server = setupServer(...globalHandlers);


describe('CourseDetail Integration', () => {
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => {
        server.resetHandlers();
        vi.restoreAllMocks();
    });
    afterAll(() => server.close());

   it('loads all data and highlights the current user', async () => {
        server.use(
            http.get('**/api/courses/1/participants', () => {
                return HttpResponse.json({
                    instructors: [{ id: 99, firstName: 'Prof.', lastName: 'Zylos', email: 'prof@zylos.de' }],
                    students: [{ id: 1, firstName: 'Sascha', lastName: 'S.', email: 'sascha@test.de' }]
                });
            }),
            http.get('**/api/courses/1/materials', () => {
                return HttpResponse.json([
                    { id: 1, title: 'Test Material', fileName: 'test.pdf', contentType: 'application/pdf' }
                ]);
            })
        );

        const authSpy = vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: 1, firstName: 'Sascha', lastName: 'S.' },
            login: vi.fn(),
            logout: vi.fn(),
            loading: false,
            isAuthenticated: true
        } as any);

        renderWithAuthAndRouter(<CourseDetail />, ['/courses/1'], '/courses/:id');

        expect(await screen.findByText(/Generischer Kurs 1/i)).toBeInTheDocument();
        expect(screen.getByText(/Test Material/i)).toBeInTheDocument();

        expect(await screen.findByText(/Sascha S\. \(Du\)/)).toBeInTheDocument();
    });

    it('shows an error message if the course api call fails', async () => {
        server.use(
            http.get(`*/courses/1`, () => {
                return new HttpResponse(null, { status: 404 });
            })
        );

        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: '1' },
            login: vi.fn(),
            logout: vi.fn(),
            loading: false,
            isAuthenticated: true
        } as any);

        renderWithAuthAndRouter(<CourseDetail />, ['/courses/1']);

        expect(await screen.findByText(/Kurs nicht gefunden/i)).toBeInTheDocument();
    });
});