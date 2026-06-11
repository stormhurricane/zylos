import React from 'react';
import { render } from '@testing-library/react';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext'; 

/**
 * Renders every component wrapped in AuthProvider and a v7-compatible Router.
 * Perfect for testing Login, Registration, or protected routes.
 */
export const renderWithAuthAndRouter = (ui: React.ReactElement, initialEntries = ['/']) => {
    const routerOptions = {
        initialEntries,
        future: {
            v7_startTransition: true,
            v7_relativeSplatPath: true,
        } as any
    };

    const router = createMemoryRouter(
        [{ path: '*', element: ui }], 
        routerOptions
    );

    return render(
        <AuthProvider>
            <RouterProvider router={router} />
        </AuthProvider>
    );
};