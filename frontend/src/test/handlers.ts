import { http, HttpResponse } from 'msw';

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

    http.get('*/courses/:id', () => {
        return HttpResponse.json({ id: 1, title: 'Standard Kurs' });
    })
];