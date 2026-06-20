import { screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, beforeAll, afterEach, afterAll, vi } from 'vitest';
import { CourseDetail } from './CourseDetail';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import * as AuthContext from '../../context/AuthContext';
import { globalHandlers, TEST_BASE_URL } from '../../test/handlers';

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
            http.get(`${TEST_BASE_URL}/courses/1/participants`, () => {
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

        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: 1, firstName: 'Sascha', lastName: 'S.' },
            login: vi.fn(),
            logout: vi.fn(),
            loading: false,
            isAuthenticated: true,
            isInstructor: false
        } as any);

        renderWithAuthAndRouter(<CourseDetail />, ['/courses/1'], '/courses/:id');

        expect(await screen.findByText(/Generischer Kurs 1/i)).toBeInTheDocument();
        expect(screen.getByText(/Test Material/i)).toBeInTheDocument();
        expect(screen.getByText(/Sascha S\. \(Du\)/)).toBeInTheDocument();
    });

    it('shows an error message if the course api call fails', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

        server.use(
            http.get(`${TEST_BASE_URL}/courses/1`, () => {
                return HttpResponse.json(
                    { error: 'Kurs existiert nicht oder ist archiviert.' }, 
                    { status: 404 }
                );
            })
        );

        vi.spyOn(AuthContext, 'useAuth').mockReturnValue({
            user: { userId: 1 },
            login: vi.fn(),
            logout: vi.fn(),
            loading: false,
            isAuthenticated: true,
            isInstructor: false
        } as any);

        renderWithAuthAndRouter(<CourseDetail />, ['/courses/1'], '/courses/:id');

        expect(await screen.findByTestId('error-state')).toHaveTextContent('Kurs existiert nicht oder ist archiviert.');

        consoleSpy.mockRestore();
    });
});