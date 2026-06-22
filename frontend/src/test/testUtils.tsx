import React from 'react';
import { render } from '@testing-library/react';
import { createMemoryRouter, RouterProvider } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext'; 

export const renderWithAuthAndRouter = (
    ui: React.ReactElement, 
    initialEntries = ['/'],
    routePath = '*' 
) => {
    const routerOptions = {
        initialEntries,
        future: {
            v7_startTransition: true,
            v7_relativeSplatPath: true,
        }
    };

    const routes = [
        { path: routePath, element: ui },
        { path: '*', element: ui } // Fallback Sicherheitsnetz für redirects
    ];

    const router = createMemoryRouter(routes, routerOptions);

    return render(
        <AuthProvider>
            <RouterProvider router={router} />
        </AuthProvider>
    );
};

export const renderWithRouter = (ui: React.ReactElement, initialEntries = ['/']) => {
    const routerOptions = {
        initialEntries,
        future: {
            v7_startTransition: true,
            v7_relativeSplatPath: true,
        }
    };

    const router = createMemoryRouter(
        [{ path: '*', element: ui }], 
        routerOptions
    );

    return render(<RouterProvider router={router} />);
};