import { http, HttpResponse } from 'msw';
import { SemesterTerm } from '../api/types';

const baseUrl = 'http://localhost:8080/api';

export const globalHandlers = [
    // Login-endpoint 
    http.post(`${baseUrl}/users/login`, async ({ request }) => {
        const body = (await request.json()) as any;
        return HttpResponse.json({ accessToken: 'mocked-jwt-token', role: 'STUDENT' });
    }),

    // happy path for register
    http.post(`${baseUrl}/users/register/student`, async () => {
        return HttpResponse.json({ success: true }, { status: 201 });
    }),

     http.post(`${baseUrl}/users/register/teacher`, async () => {
        return HttpResponse.json({ success: true }, { status: 201 });
    }),

    // global handler to get a profile
    http.get(`${baseUrl}/users/:id`, ({ params }) => {
        const { id } = params;
        return HttpResponse.json({
            id: id,
            firstName: 'Jojen',
            lastName: 'Doe',
            email: 'own@uni.de',
            matriculationNumber: '1000001',
            studySubject: 'Informatik',
            privateAddress: 'Musterstraße 1'
        });
    }),

    http.get(`${baseUrl}/courses/my-enrollments`, () => {
        return HttpResponse.json([
            { id: 1, title: 'Software Engineering', term: SemesterTerm.WINTER, academicYear: '2026' }
        ]);
    }),

    http.get(`${baseUrl}/courses/:id`, ({ params }) => {
        const { id } = params;
        
        // Sicherheitsnetz: Falls das Routing im Test fehlschlägt und 'undefined' schickt
        if (id === 'undefined' || !id) {
            return new HttpResponse(null, { status: 404 });
        }

        return HttpResponse.json({
            // WICHTIG: Als Number zurückgeben, weil dein Hook mit Number(id) arbeitet!
            id: Number(id), 
            title: `Generischer Kurs ${id}`,
            type: 'LECTURE',
            term: 'SUMMER',
            academicYear: '2026'
        });
    }),

    http.get(`${baseUrl}/courses/:id`, () => {
        return HttpResponse.json({ id: 1, title: 'Standard Kurs' });
    })
];