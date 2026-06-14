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

const mockedCourseApi = vi.mocked(courseApi);

/**
 * Helper to render the CourseList with necessary providers.
 */
const renderCourseList = () => {
    return render(
        <BrowserRouter>
            <AuthProvider>
                <CourseList />
            </AuthProvider>
        </BrowserRouter>
    );
};

describe('CourseList Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        window.alert = vi.fn();
    });

    it('renders courses and handles enrollment', async () => {
        const mockCourse = { id: 1, title: 'Software Engineering', type: 'LECTURE', term: 'SUMMER', academicYear: '2024' };
        
        mockedCourseApi.getAllCourses.mockResolvedValue([mockCourse]  as any);
        mockedCourseApi.getMyCourses.mockResolvedValue([] as any);
        mockedCourseApi.enroll.mockResolvedValue({} as any);

        renderCourseList();

        // Verify the course title is rendered
        expect(await screen.findByText('Software Engineering')).toBeInTheDocument();
        
        // Button should show "Teilnehmen" (Enroll)
        const enrollButton = screen.getByRole('button', { name: /Teilnehmen/i });
        fireEvent.click(enrollButton);

        await waitFor(() => {
            expect(mockedCourseApi.enroll).toHaveBeenCalledWith(1);
            expect(window.alert).toHaveBeenCalledWith('Erfolgreich eingeschrieben!');
        });
    });

    it('shows "Ansehen" button if already enrolled', async () => {
        const mockCourse = { id: 1, title: 'Software Engineering', type: 'LECTURE', term: 'SUMMER', academicYear: '2024' };
        mockedCourseApi.getAllCourses.mockResolvedValue( [mockCourse]  as any);
        mockedCourseApi.getMyCourses.mockResolvedValue( [mockCourse] as any);

        renderCourseList();

        // Button should show "Ansehen" (View) for enrolled courses
        expect(await screen.findByRole('button', { name: /Ansehen/i })).toBeInTheDocument();
    });

    it('shows empty state message when no courses are found', async () => {
        mockedCourseApi.getAllCourses.mockResolvedValue( [] as any);
        mockedCourseApi.getMyCourses.mockResolvedValue( [] as any);

        renderCourseList();

        // Check for the "no courses found" message
        expect(await screen.findByText(/Keine Lehrveranstaltungen gefunden/i)).toBeInTheDocument();
    });

    it('shows error message when API fails', async () => {
        // Mock a rejection for the API call
        mockedCourseApi.getAllCourses.mockRejectedValue(new Error('API Error'));
        mockedCourseApi.getMyCourses.mockResolvedValue( [] as any);

        renderCourseList();

        // Verify the error message is displayed to the user
        expect(await screen.findByText(/Fehler beim Laden der Kurse/i)).toBeInTheDocument();
    });
});