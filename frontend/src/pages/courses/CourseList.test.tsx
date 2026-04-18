import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { CourseList } from './CourseList';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import { courseApi } from '../../api/courseApi';

vi.mock('../../api/courseApi', () => ({
    courseApi: {
        getAllCourses: vi.fn(),
        getMyCourses: vi.fn(),
        enroll: vi.fn(),
    },
}));

describe('CourseList Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('renders courses and handles enrollment', async () => {
        const mockCourse = { id: 1, title: 'Software Engineering', type: 'LECTURE', term: 'SUMMER', academicYear: '2024' };
        
        (courseApi.getAllCourses as any).mockResolvedValue({ data: [mockCourse] });
        (courseApi.getMyCourses as any).mockResolvedValue({ data: [] });
        (courseApi.enroll as any).mockResolvedValue({});
        
        window.alert = vi.fn();

        render(
            <BrowserRouter>
                <AuthProvider>
                    <CourseList />
                </AuthProvider>
            </BrowserRouter>
        );

        // Prüfen, ob der Kurs angezeigt wird
        expect(await screen.findByText('Software Engineering')).toBeInTheDocument();
        
        // Button sollte "Teilnehmen" zeigen
        const enrollButton = screen.getByText('Teilnehmen');
        fireEvent.click(enrollButton);

        await waitFor(() => {
            expect(courseApi.enroll).toHaveBeenCalledWith(1);
            expect(window.alert).toHaveBeenCalledWith('Erfolgreich eingeschrieben!');
        });
    });

    it('shows "Ansehen" button if already enrolled', async () => {
        const mockCourse = { id: 1, title: 'Software Engineering', type: 'LECTURE', term: 'SUMMER', academicYear: '2024' };
        (courseApi.getAllCourses as any).mockResolvedValue({ data: [mockCourse] });
        (courseApi.getMyCourses as any).mockResolvedValue({ data: [mockCourse] });

        render(<BrowserRouter><AuthProvider><CourseList /></AuthProvider></BrowserRouter>);

        expect(await screen.findByText('Ansehen')).toBeInTheDocument();
    });
});