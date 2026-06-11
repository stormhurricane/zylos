import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { Profile } from './Profile';
import { useProfile } from './useProfile';
import { MemoryRouter } from 'react-router-dom';
import { SemesterTerm, CourseType } from '../../api/types';
import { renderWithAuthAndRouter } from '../../test/testUtils';

// Mocking useProfile Hook 
vi.mock('./useProfile', () => ({
    useProfile: vi.fn()
}));

// Mock useNavigate due to Zurück Button
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual,
        useNavigate: () => mockNavigate,
        useParams: () => ({ id: '123' }) // simulate /profile/123
    };
});

describe('Profile Component Integration', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should show Loadingscreen when loading data', () => {
        vi.mocked(useProfile).mockReturnValue({
            profile: null,
            courses: [],
            loading: true, // triggers page loader
            error: null,
            isOwnProfile: false
        });

        renderWithAuthAndRouter(<Profile />);

        // assert
        expect(screen.getByText('Profil wird geladen...')).toBeInTheDocument();
    });

    it('should render own profile with courses correctly', () => {
        vi.mocked(useProfile).mockReturnValue({
            profile: { id: 123, firstName: 'Jojen', lastName: 'Doe', email: 'own@uni.de', privateAddress: 'Musterstraße 1'},
            courses: [{ id: 1, title: 'Software Engineering', term: SemesterTerm.WINTER, academicYear: '2026', type: CourseType.LECTURE}],
            loading: false,
            error: null,
            isOwnProfile: true
        }); 

        renderWithAuthAndRouter(<Profile />);

        // assert that profile data is displayed
        expect(screen.getByText('Jojen Doe')).toBeInTheDocument();
        expect(screen.getByText('own@uni.de')).toBeInTheDocument();
        expect(screen.getByText('Musterstraße 1')).toBeInTheDocument();

        // assert that courses are displayed
        expect(screen.getByText('Software Engineering')).toBeInTheDocument();
        expect(screen.getByText('Meine Kurse')).toBeInTheDocument();

    });

    it('should render other profile without courses and private address', () => {
        vi.mocked(useProfile).mockReturnValue({
            profile: { id: 999, firstName: 'Max', lastName: 'Mustermann', email: 'stranger@uni.de', privateAddress: 'Musterstraße 1'},
            courses: [], // no courses for other profiles
            loading: false,
            error: null,
            isOwnProfile: false
        });  

        renderWithAuthAndRouter(<Profile />);
    

        // assert that profile data is displayed
        expect(screen.getByText('Max Mustermann')).toBeInTheDocument();
        expect(screen.getByText('stranger@uni.de')).toBeInTheDocument();

        expect(screen.queryByText('Meine Kurse')).not.toBeInTheDocument();   
        expect(screen.queryByText('Musterstraße 1')).not.toBeInTheDocument();

        const editButton = screen.queryByRole('button', { name: /Profil bearbeiten/i });
        expect(editButton).not.toBeInTheDocument();
    });

    it('zeigt eine Meldung an, wenn das Profil nicht existiert', () => {
        vi.mocked(useProfile).mockReturnValue({
            profile: null, // no Profile found
            courses: [],
            loading: false,
            error: null,
            isOwnProfile: false
        });

        renderWithAuthAndRouter(<Profile />);

        expect(screen.getByText('Profil nicht gefunden.')).toBeInTheDocument();
        
        expect(screen.queryByRole('heading', { level: 1 })).not.toBeInTheDocument();
    });

});