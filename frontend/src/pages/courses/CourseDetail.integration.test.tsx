import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, beforeAll, afterEach, afterAll, vi } from 'vitest';
import { CourseDetail } from './CourseDetail';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';

vi.mock('../../context/AuthContext', () => ({
    useAuth: () => ({
        user: { userId: 1, firstName: 'Sascha', lastName: 'S.' },
        loading: false
    }),
    AuthProvider: ({ children }: { children: React.ReactNode }) => <div>{children}</div>
}));

// 1. MSW Server Setup: Hier definieren wir, wie die API antworten soll
const handlers = [
    http.get('*/api/courses/1', () => {
        return HttpResponse.json({
            id: 1,
            title: 'Integration Test Kurs',
            type: 'LECTURE',
            term: 'SUMMER',
            academicYear: '2024'
        });
    }),
    http.get('*/api/courses/1/participants', () => {
        return HttpResponse.json({
            instructors: [{ id: 99, firstName: 'Prof.', lastName: 'Zylos', email: 'prof@zylos.de' }],
            students: [{ id: 1, firstName: 'Sascha', lastName: 'S.', email: 'sascha@test.de' }]
        });
    }),
    http.get('*/api/courses/1/materials', () => {
        return HttpResponse.json([
            { id: 1, title: 'Test Material', fileName: 'test.pdf', contentType: 'application/pdf' }
        ]);
    })
];

const server = setupServer(...handlers);

describe('CourseDetail Integration', () => {
    beforeAll(() => server.listen());
    afterEach(() => server.resetHandlers());
    afterAll(() => server.close());

    it('lädt alle Daten und hebt den aktuellen Nutzer ("Du") hervor', async () => {
        // Wir gehen davon aus, dass der AuthProvider im Test 
        // einen User mit id: 1 bereitstellt (z.B. durch Mocking des Tokens)
        
        render(
            <MemoryRouter initialEntries={['/courses/1']}>
                <AuthProvider>
                    <Routes>
                        <Route path="/courses/:id" element={<CourseDetail />} />
                    </Routes>
                </AuthProvider>
            </MemoryRouter>
        );

        // Warten auf den Kurs-Titel (Wichtig: findAllBy... wartet auf async Änderungen)
        expect(await screen.findByText('Integration Test Kurs')).toBeInTheDocument();

        // Prüfen, ob Materialien geladen wurden
        expect(screen.getByText('Test Material')).toBeInTheDocument();

        // Wichtigster Teil: Das Hervorheben des eigenen Nutzers (ID 1)
        // Da unser MSW Handler Sascha S. mit ID 1 zurückgibt, muss "(Du)" erscheinen.
        await waitFor(() => {
            const me = screen.getByText((content, element) => {
                const hasText = element?.textContent === 'Sascha S. (Du)';
                const childrenDontHaveText = Array.from(element?.children || []).every(
                    (child) => child.textContent !== 'Sascha S. (Du)'
                );
                return hasText && childrenDontHaveText;
            });
            expect(me).toBeInTheDocument();
            // Hinweis: getHaveStyle funktioniert bei CSS-Variablen oft nur, wenn das Theme geladen ist.
            // Wir prüfen primär die Existenz des Textes.
        });
    });

    it('zeigt eine Fehlermeldung, wenn der Kurs-Call fehlschlägt', async () => {
        server.use(
            http.get('*/api/courses/1', () => {
                return new HttpResponse(null, { status: 404 });
            })
        );

        render(
            <MemoryRouter initialEntries={['/courses/1']}>
                <AuthProvider>
                    <Routes>
                        <Route path="/courses/:id" element={<CourseDetail />} />
                    </Routes>
                </AuthProvider>
            </MemoryRouter>
        );

        expect(await screen.findByText(/Kurs nicht gefunden/i)).toBeInTheDocument();
    });
});