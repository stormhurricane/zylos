import '@testing-library/jest-dom/vitest';
import { setupServer } from 'msw/node';

import { handlers } from './setupHandlers';
import { afterAll, afterEach, beforeAll } from 'vitest';

export const server = setupServer(...handlers);

beforeAll(() => server.listen());
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
