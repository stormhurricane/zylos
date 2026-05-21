import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { CourseCreate } from './CourseCreate';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import { courseApi } from '../../api/courseApi';
import { CourseType, SemesterTerm } from '../../api/types';

vi.mock('../../api/courseApi', () => ({
    courseApi: {
        createCourse: vi.fn(),
        importCsv: vi.fn(),
    },
}));

/**
 * Helper function to render the component with all necessary context providers.
 */
const renderCourseCreate = () => {
    return render(
        <BrowserRouter>
            <AuthProvider>
                <CourseCreate />
            </AuthProvider>
        </BrowserRouter>
    );
};

describe('CourseCreate Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('submits manual course creation successfully', async () => {
        (courseApi.createCourse as any).mockResolvedValue({ data: { id: 100 } });

        renderCourseCreate();

        // Fill out the form
        fireEvent.change(screen.getByPlaceholderText(/z.B. Software Engineering/i), { target: { value: 'Test Kurs' } });
        fireEvent.change(screen.getByPlaceholderText(/z.B. 2024/i), { target: { value: '2025' } });
        
        // Submit the form
        fireEvent.click(screen.getByRole('button', { name: /Erstellen/i }));

        await waitFor(() => {
            expect(courseApi.createCourse).toHaveBeenCalledWith({
                title: 'Test Kurs',
                type: CourseType.LECTURE,
                term: SemesterTerm.SUMMER,
                academicYear: '2025'
            });
        });
        expect(screen.getByText('Lehrveranstaltung erfolgreich angelegt!')).toBeInTheDocument();
    });

    it('shows error message when manual creation fails', async () => {
        (courseApi.createCourse as any).mockRejectedValue(new Error('API Error'));

        renderCourseCreate();

        fireEvent.change(screen.getByPlaceholderText(/z.B. Software Engineering/i), { target: { value: 'Fail Kurs' } });
        fireEvent.change(screen.getByPlaceholderText(/z.B. 2024/i), { target: { value: '2025' } });
        fireEvent.click(screen.getByRole('button', { name: /Erstellen/i }));

        await waitFor(() => {
            expect(screen.getByText(/Fehler beim manuellen Anlegen/i)).toBeInTheDocument();
        });
    });

    it('handles CSV import successfully', async () => {
        (courseApi.importCsv as any).mockResolvedValue({ data: [{ id: 1 }, { id: 2 }] });

        renderCourseCreate();

        // Mock a file selection
        const file = new File(['title;type;term;year'], 'test.csv', { type: 'text/csv' });
        const input = screen.getByLabelText(/CSV Import/i) as HTMLInputElement;
        fireEvent.change(input, { target: { files: [file] } });

        fireEvent.click(screen.getByRole('button', { name: /CSV hochladen/i }));

        await waitFor(() => {
            expect(courseApi.importCsv).toHaveBeenCalled();
            expect(screen.getByText(/2 Lehrveranstaltungen erfolgreich importiert/i)).toBeInTheDocument();
        });
    });
});
