import { http, HttpResponse } from 'msw';
import { SemesterTerm } from '../api/types';

export const globalHandlers = [
    // Login-endpoint 
    http.post('*/users/login', async ({ request }) => {
        const body = (await request.json()) as any;
        return HttpResponse.json({ accessToken: 'mocked-jwt-token', role: 'STUDENT' });
    }),

    // happy path for register
    http.post('*/users/register/student', async () => {
        return HttpResponse.json({ success: true }, { status: 201 });
    }),

     http.post('*/users/register/teacher', async () => {
        return HttpResponse.json({ success: true }, { status: 201 });
    }),

    // global handler to get a profile
    http.get('*/users/:id', ({ params }) => {
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

    http.get('*/courses/my-enrollments', () => {
        return HttpResponse.json([
            { id: 1, title: 'Software Engineering', term: SemesterTerm.WINTER, academicYear: '2026' }
        ]);
    }),

    http.get('*/courses/:id', () => {
        return HttpResponse.json({ id: 1, title: 'Standard Kurs' });
    })
];