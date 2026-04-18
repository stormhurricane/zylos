import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, beforeAll, afterEach, afterAll } from 'vitest';
import { Profile } from './Profile';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';

const handlers = [
    http.get('*/api/users/me', () => {
        return HttpResponse.json({
            id: 1,
            firstName: 'Sascha',
            lastName: 'S.',
            email: 'sascha@test.de',
            matriculationNumber: '1000001'
        });
    }),
    http.get('*/api/courses/my-enrollments', () => {
        return HttpResponse.json([
            { id: 1, title: 'Software Engineering', term: 'SUMMER', academicYear: '2024' },
            { id: 2, title: 'Datenbanken', term: 'WINTER', academicYear: '2024' }
        ]);
    })
];

const server = setupServer(...handlers);

describe('Profile Integration', () => {
    beforeAll(() => server.listen());
    afterEach(() => server.resetHandlers());
    afterAll(() => server.close());

    it('zeigt die Kursliste des Nutzers im eigenen Profil an', async () => {
        render(
            <BrowserRouter>
                <AuthProvider>
                    <Profile />
                </AuthProvider>
            </BrowserRouter>
        );

        // Profil laden prüfen
        expect(await screen.findByText('Sascha S.')).toBeInTheDocument();

        // Kurs-Kacheln prüfen
        expect(await screen.findByText('Software Engineering')).toBeInTheDocument();
        expect(screen.getByText('Datenbanken')).toBeInTheDocument();
        
        // Prüfen, ob Header "Meine Kurse" existiert
        expect(screen.getByText('Meine Kurse')).toBeInTheDocument();
    });
});