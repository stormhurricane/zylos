import { http, HttpResponse } from 'msw';

import { type ErrorResponse, type RegisterRequest } from '../types/index';

const RegisterHandler = http.post('/api/v1/auth/register',
    async ({request}) => {
        const body = (await request.json()) as RegisterRequest;

        if(body.username == 'existinguser') {
            return HttpResponse.json<ErrorResponse>({
                status: 400,
                message: 'Registration failed due to conflicting user data',
                timestamp: new Date().toISOString(),
                errors: {
                    username: 'Username is already in use'
                }
            });
        }

        if(body.email == 'crash@example.com') {
            return HttpResponse.json<ErrorResponse>({
                status: 500,
                message: 'An unexpected Error occured',
                timestamp: new Date().toISOString(),
                errors: undefined,
            });
        }

        if(body.email == "invalidemail") {
            return HttpResponse.json<ErrorResponse>({
                status: 400,
                message: 'Invalid Request body',
                timestamp: new Date().toISOString(),
                errors: {
                    email: 'Email should be valid'
                }
            })
        }

        return HttpResponse.json({status: 204});
    }
);

export const handlers = [ 
    RegisterHandler
];

