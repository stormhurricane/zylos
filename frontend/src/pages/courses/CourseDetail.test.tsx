import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach, type Mock } from 'vitest';
import { CourseDetail } from './CourseDetail';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from '../../context/AuthContext';
import { courseApi } from '../../api/courseApi';

vi.mock('../../api/courseApi', () => ({
    courseApi: {
        getCourseById: vi.fn(),
        getParticipants: vi.fn(),
        getMaterials: vi.fn(),
    },
}));

/**
 * Helper function to render the CourseDetail component with all necessary context providers.
 */
const renderCourseDetail = (id: string = '1') => {
    return render(
        <MemoryRouter initialEntries={[`/courses/${id}`]}>
            <AuthProvider>
                <Routes>
                    <Route path="/courses/:id" element={<CourseDetail />} />
                </Routes>
            </AuthProvider>
        </MemoryRouter>
    );
};

describe('CourseDetail Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('renders course info, materials and participants', async () => {
        (courseApi.getCourseById as Mock).mockResolvedValue({
            data: { id: 1, title: 'Deep Dive React', type: 'SEMINAR', term: 'WINTER', academicYear: '2024' }
        });
        (courseApi.getParticipants as Mock).mockResolvedValue({
            data: { 
                instructors: [{ id: 10, firstName: 'Dr.', lastName: 'Zylos' }], 
                students: [{ id: 20, firstName: 'Sascha', lastName: 'S.' }] 
            }
        });
        (courseApi.getMaterials as Mock).mockResolvedValue({ 
            data: [{ id: 1, title: 'Skript 1', fileName: 'skript1.pdf' }] 
        });

        renderCourseDetail('1');

        // Check header (using role for better accessibility testing)
        expect(await screen.findByRole('heading', { name: /Deep Dive React/i })).toBeInTheDocument();
        
        // Check materials
        expect(screen.getByText('Skript 1')).toBeInTheDocument();
        
        // Check participants
        expect(screen.getByText('Dr. Zylos')).toBeInTheDocument();
        expect(screen.getByText('Sascha S.')).toBeInTheDocument();
    });

    it('shows error message when course is not found', async () => {
        // Mock a failed API call
        (courseApi.getCourseById as Mock).mockRejectedValue(new Error('Not Found'));

        renderCourseDetail('999');

        // Verify the error message is displayed
        expect(await screen.findByText(/Kurs nicht gefunden/i)).toBeInTheDocument();
    });
});
