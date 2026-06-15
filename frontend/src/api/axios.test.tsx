// src/api/axios.test.ts
import api from './axios';
import { sessionService } from '../utils/sessionService';
import { http, HttpResponse } from 'msw';
import { setupServer } from 'msw/node';
import { globalHandlers } from '../test/handlers';

const server = setupServer(...globalHandlers);

vi.mock('../utils/tokenService', () => ({
    tokenService: {
        getToken: () => {},
        clearToken: () => {},
    }
}));

describe('Axios Interceptors', () => {
    const targetUrl = 'http://localhost:8080/api/test-endpoint';

    beforeEach(() => {
        vi.clearAllMocks();
    });
    beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
    afterEach(() => server.resetHandlers());
    afterAll(() => server.close());

    it('should inject Bearer token into headers if present', async () => {
        const getTokenSpy = vi.spyOn(sessionService, 'getToken').mockReturnValue('valid-test-token');
        
        server.use(
            http.get(targetUrl, ({ request }) => {
                const authHeader = request.headers.get('Authorization');
                return HttpResponse.json({ authHeader });
            })
        );

        const response = await api.get<any, { authHeader: string }>('/test-endpoint');

        expect(getTokenSpy).toHaveBeenCalled();
        expect(response.authHeader).toBe('Bearer valid-test-token');
        
        getTokenSpy.mockRestore();
    });

    it('should handle 401 errors, clear token and dispatch event', async () => {
        const clearTokenSpy = vi.spyOn(sessionService, 'clearSession').mockImplementation(() => {});

        server.use(
            http.get(targetUrl, () => {
                return new HttpResponse(null, { status: 401 });
            })
        );

        const eventSpy = vi.fn();
        window.addEventListener('auth-unauthorized', eventSpy);

        await expect(api.get('/test-endpoint')).rejects.toThrow();

        expect(clearTokenSpy).toHaveBeenCalled();
        expect(eventSpy).toHaveBeenCalled();

        window.removeEventListener('auth-unauthorized', eventSpy);
        clearTokenSpy.mockRestore();
    });
});