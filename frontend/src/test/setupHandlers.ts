import { http, HttpResponse } from 'msw';


const RegisterHandler = http.post('**/api/v1/auth/register',
    () => {
        return HttpResponse.json(null, {status: 204});
    }
);

export const handlers = [ 
    RegisterHandler
];

