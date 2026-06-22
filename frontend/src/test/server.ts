import { setupServer } from 'msw/node';
import { globalHandlers } from './handlers'; 

export const server = setupServer(...globalHandlers);