import { screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { Navbar } from './Navbar';
import * as useNavbarModule from './useNavbar';
import { renderWithAuthAndRouter } from '../test/testUtils'; 

vi.mock('./useNavbar', () => ({
    useNavbar: vi.fn()
}));

describe('Navbar Component', () => {
    const mockUseNavbar = vi.mocked(useNavbarModule.useNavbar);
    const mockHandleSearchSubmit = vi.fn();
    const mockLogout = vi.fn();
    const mockSetSearchQuery = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
        
        mockUseNavbar.mockReturnValue({
            isInstructor: false,
            searchQuery: '',
            setSearchQuery: mockSetSearchQuery,
            handleSearchSubmit: mockHandleSearchSubmit,
            logout: mockLogout
        });
    });

    it('should render the brand name Zylos', () => {
        renderWithAuthAndRouter(<Navbar />);
        
        expect(screen.getByText('Zylos')).toBeInTheDocument();
        expect(screen.queryByText('Kurs erstellen')).not.toBeInTheDocument();
    });

    it('should render "Kurs erstellen" only if user is an instructor', () => {
        mockUseNavbar.mockReturnValue({
            isInstructor: true,
            searchQuery: '',
            setSearchQuery: mockSetSearchQuery,
            handleSearchSubmit: mockHandleSearchSubmit,
            logout: mockLogout
        });

        renderWithAuthAndRouter(<Navbar />);
        expect(screen.getByText('Kurs erstellen')).toBeInTheDocument();
    });

    it('should trigger setSearchQuery on input change', () => {
        renderWithAuthAndRouter(<Navbar />);
        const input = screen.getByPlaceholderText(/Nach Studenten oder Dozenten suchen/i);
        
        fireEvent.change(input, { target: { value: 'Mustermann' } });
        
        expect(mockSetSearchQuery).toHaveBeenCalledWith('Mustermann');
    });

    it('should call logout when logout button is clicked', () => {
        renderWithAuthAndRouter(<Navbar />);
        const logoutBtn = screen.getByRole('button', { name: /logout/i });
        
        fireEvent.click(logoutBtn);
        
        expect(mockLogout).toHaveBeenCalledTimes(1);
    });
});