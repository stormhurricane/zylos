import { http, HttpResponse } from 'msw';
import { type UserResponse } from '../types';


const RegisterHandler = http.post('**/api/v1/auth/register',
    () => {
        return HttpResponse.json(null, {status: 204});
    }
);

const MeHandler = http.get('**/api/v1/auth/me',
    () => {
        const userResponse : UserResponse = {
            id: "uniqueUIID",
            username: "johndoe",
            email: "john.doe@example.com",
            roles: ["STUDENT"]
        };
        return HttpResponse.json(userResponse, { status: 200 });
    }
);

export const handlers = [ 
    RegisterHandler,
    MeHandler
];

