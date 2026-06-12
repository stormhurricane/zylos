import { screen } from '@testing-library/react';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { ProfileEdit } from './ProfileEdit';
import { useProfileEdit } from './useProfileEdit';
import { renderWithRouter } from '../../test/testUtils'; 

vi.mock('./useProfileEdit', () => ({
    useProfileEdit: vi.fn()
}));

describe('ProfileEdit Component UI', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should render the ProfileEdit component with loading state', () => {
        vi.mocked(useProfileEdit).mockReturnValue({
            loading: true,
        } as any);

        renderWithRouter(<ProfileEdit />);

        expect(screen.getByText('Einstellungen werden geladen...')).toBeInTheDocument();
        
    });

    it('should display the specific fields for a student', () => {
        vi.mocked(useProfileEdit).mockReturnValue({
            loading: false,
            isStudent: true,
            displayName: { firstName: 'Max', lastName: 'Mustermann' },
            formData: { studySubject: 'Informatik', privateAddress: 'Musterweg 5' }
        } as any);
        
        renderWithRouter(<ProfileEdit />);

        expect(screen.getByText('Studienfach')).toBeInTheDocument();
        expect(screen.queryByText('Lehrstuhl')).not.toBeInTheDocument();
    });

    it('should display the specific fields for a lecturer/employee', () => {
        vi.mocked(useProfileEdit).mockReturnValue({
            loading: false,
            isStudent: false,
            displayName: { firstName: 'Dr.', lastName: 'Schmidt' },
            formData: { chair: 'Informatik', researchArea: 'KI', privateAddress: 'Lehrstuhlweg 1' }
        } as any);
        
        renderWithRouter(<ProfileEdit />);

        expect(screen.getByText('Lehrstuhl')).toBeInTheDocument();
        expect(screen.getByText('Forschungsgebiet')).toBeInTheDocument();
        expect(screen.queryByText('Studienfach')).not.toBeInTheDocument();
    });

    it('should display an error message when the hook returns an error', () => {
        // TIPP: Setze loading: false und error: 'Update fehlgeschlagen.'
        // Prüfe, ob der Fehlertext auf dem Bildschirm gerendert wird.
        vi.mocked(useProfileEdit).mockReturnValue({
            loading: false,
            isStudent: true,
            displayName: { firstName: 'Max', lastName: 'Mustermann' },
            formData: {},
            error: 'Update fehlgeschlagen.' // <--- Hier provozieren wir den Fehler
        } as any);

        renderWithRouter(<ProfileEdit />);

        // [Certain] Prüfen, ob der Fehlertext im Dokument auftaucht
        expect(screen.getByText('Update fehlgeschlagen.')).toBeInTheDocument();
        // Optionale Zusatzabsicherung: Hat der Text die richtige CSS-Klasse?
        expect(screen.getByText('Update fehlgeschlagen.')).toHaveClass('error-message');
    });

    it('should forward submits and cancels to the hook', () => {
        // TIPP: Erstelle Spione für die Funktionen: const mockSubmit = vi.fn(); const mockCancel = vi.fn();
        // Übergib diese im mockReturnValue an handleSubmit und cancel.
        // Simuliere Klicks auf die Buttons (fireEvent oder userEvent) und prüfe, ob die Mocks aufgerufen wurden.

        const mockSubmit = vi.fn();
        const mockCancel = vi.fn();

        vi.mocked(useProfileEdit).mockReturnValue({
            loading: false,
            isStudent: true,
            displayName: { firstName: 'Max', lastName: 'Mustermann' },
            formData: {},
            error: '',
            handleChange: vi.fn(),
            handleFileChange: vi.fn(),
            handleSubmit: mockSubmit, // Spion verdrahten
            cancel: mockCancel         // Spion verdrahten
        } as any);

        renderWithRouter(<ProfileEdit />);

        // 1. Cancel-Button testen
        const cancelButton = screen.getByRole('button', { name: /abbrechen/i });
        cancelButton.click(); // Ein simpler nackter Klick reicht bei synchronen Mocks vollkommen aus
        expect(mockCancel).toHaveBeenCalledTimes(1);

        // 2. Submit-Button testen
        const submitButton = screen.getByRole('button', { name: /änderungen speichern/i });
        submitButton.click();
        expect(mockSubmit).toHaveBeenCalledTimes(1);
    });
});