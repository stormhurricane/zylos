import { http, HttpResponse } from 'msw';

export const globalHandlers = [
    // Login-endpoint 
    http.post('*/users/login', async ({ request }) => {
        const body = (await request.json()) as any;
        // wrong user
        if (body.identifier === 'wrong@user.de') {
            return new HttpResponse({ error: 'Ungültige Zugangsdaten' }, { status: 401 });
        }
        return HttpResponse.json({ accessToken: 'mocked-jwt-token', role: 'STUDENT' });
    }),

    http.get('*/courses/:id', () => {
        return HttpResponse.json({ id: 1, title: 'Standard Kurs' });
    })
];