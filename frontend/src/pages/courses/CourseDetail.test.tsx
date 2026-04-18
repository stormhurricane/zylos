import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
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

describe('CourseDetail Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('renders course info, materials and participants', async () => {
        (courseApi.getCourseById as any).mockResolvedValue({
            data: { id: 1, title: 'Deep Dive React', type: 'SEMINAR', term: 'WINTER', academicYear: '2024' }
        });
        (courseApi.getParticipants as any).mockResolvedValue({
            data: { 
                instructors: [{ id: 10, firstName: 'Dr.', lastName: 'Zylos' }], 
                students: [{ id: 20, firstName: 'Sascha', lastName: 'S.' }] 
            }
        });
        (courseApi.getMaterials as any).mockResolvedValue({ 
            data: [{ id: 1, title: 'Skript 1', fileName: 'skript1.pdf' }] 
        });

        render(
            <MemoryRouter initialEntries={['/courses/1']}>
                <AuthProvider>
                    <Routes>
                        <Route path="/courses/:id" element={<CourseDetail />} />
                    </Routes>
                </AuthProvider>
            </MemoryRouter>
        );

        // Header prüfen
        expect(await screen.findByText('Deep Dive React')).toBeInTheDocument();
        
        // Materialien prüfen
        expect(screen.getByText('Skript 1')).toBeInTheDocument();
        
        // Teilnehmer prüfen
        expect(screen.getByText('Dr. Zylos')).toBeInTheDocument();
        expect(screen.getByText('Sascha S.')).toBeInTheDocument();
    });
});
