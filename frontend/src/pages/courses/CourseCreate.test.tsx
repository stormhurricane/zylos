import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { CourseCreate } from './CourseCreate';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import { courseApi } from '../../api/courseApi';

vi.mock('../../api/courseApi', () => ({
    courseApi: {
        createCourse: vi.fn(),
        importCsv: vi.fn(),
    },
}));

describe('CourseCreate Component', () => {

    it('submits manual course creation successfully', async () => {
        (courseApi.createCourse as any).mockResolvedValue({ data: { id: 100 } });

        render(
            <BrowserRouter>
                <AuthProvider>
                    <CourseCreate />
                </AuthProvider>
            </BrowserRouter>
        );

        // Formular ausfüllen
        fireEvent.change(screen.getByPlaceholderText(/z.B. Software Engineering/i), { target: { value: 'Test Kurs' } });
        fireEvent.change(screen.getByPlaceholderText(/z.B. 2024/i), { target: { value: '2025' } });
        
        // Absenden
        fireEvent.click(screen.getByRole('button', { name: /Erstellen/i }));

        await waitFor(() => {
            expect(courseApi.createCourse).toHaveBeenCalledWith({
                title: 'Test Kurs',
                type: 'LECTURE',
                term: 'SUMMER',
                academicYear: '2025'
            });
        });
        expect(screen.getByText('Lehrveranstaltung erfolgreich angelegt!')).toBeInTheDocument();
    });
});
