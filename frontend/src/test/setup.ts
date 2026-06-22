import '@testing-library/jest-dom';
import { vi, afterEach, beforeAll, afterAll } from 'vitest';
import { server } from './server'; 

beforeAll(() => {
    server.listen({ onUnhandledRequest: 'error' });
});

afterEach(() => {
    vi.clearAllMocks();
    vi.restoreAllMocks();
    server.resetHandlers(); 
});

afterAll(() => {
    server.close();
});