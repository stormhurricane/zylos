import '@testing-library/jest-dom';
import { vi, afterEach, beforeAll, afterAll } from 'vitest';
import { server } from './server'; 
import { sessionService } from '../utils/sessionService';

beforeAll(() => {
    server.listen({ onUnhandledRequest: 'error' });
});

afterEach(() => {
    vi.clearAllMocks();
    vi.restoreAllMocks();
    server.resetHandlers(); 

    sessionService.clearSession();
    sessionStorage.clear();
});

afterAll(() => {
    server.close();
});