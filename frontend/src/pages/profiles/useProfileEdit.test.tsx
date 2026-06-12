import { renderHook, waitFor, act } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useProfileEdit } from './useProfileEdit';
import { userApi } from '../../api/userApi';
import { convertFileToBase64 } from '../../utils/fileUtils';

vi.mock('../../api/userApi', () => ({
    userApi: {
        getProfile: vi.fn(),
        updateProfile: vi.fn()
    }
}));

vi.mock('../../utils/fileUtils', () => ({
    convertFileToBase64: vi.fn()
}));

const mockNavigate = vi.fn();
vi.mock('react-router-dom', () => ({
    useNavigate: () => mockNavigate
}));

describe('useProfileEdit Hook', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should load profile data and set student state correctly', async () => {
        const mockStudentProfile = {
            firstName: 'Max',
            lastName: 'Mustermann',
            privateAddress: 'Musterweg 5',
            matriculationNumber: '123456', 
            studySubject: 'Informatik',
            profilePicture: 'old-pic-base64'
        };

        vi.mocked(userApi.getProfile).mockResolvedValue(mockStudentProfile as any);

        const { result } = renderHook(() => useProfileEdit());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        // Assertions
        expect(result.current.displayName).toEqual({ firstName: 'Max', lastName: 'Mustermann' });
        expect(result.current.isStudent).toBe(true);
        expect(result.current.formData.studySubject).toBe('Informatik');
        expect(result.current.formData.privateAddress).toBe('Musterweg 5');
        expect(result.current.error).toBe('');
    });

    it('should refresh formData when input changes', async () => {
        const mockStudentProfile = {
            firstName: 'Max',
            lastName: 'Mustermann',
            privateAddress: 'Musterweg 5',
            matriculationNumber: '123456', 
            studySubject: 'Informatik',
            profilePicture: 'old-pic-base64'
        };

        vi.mocked(userApi.getProfile).mockResolvedValue(mockStudentProfile as any);

        const { result } = renderHook(() => useProfileEdit());
        
        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        result.current.handleChange({
            target: { name: 'password', value: 'Geheim123' }
        } as any);

        await waitFor(() => {
            expect(result.current.formData.password).toBe('Geheim123');
        });
    });

    it('should select a profile picture, convert, and save in state', async () => {
        const mockProfile = { firstName: 'Max', lastName: 'Mustermann', matriculationNumber: '' };
        vi.mocked(userApi.getProfile).mockResolvedValue(mockProfile as any);

        // Utility Mock: simulate a returned base64 string when convertFileToBase64 is called
        const expectedBase64 = 'data:image/png;base64,neues-bild-daten';
        vi.mocked(convertFileToBase64).mockResolvedValue(expectedBase64);

        const { result } = renderHook(() => useProfileEdit());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        // Simulate a fake File object for the browser
        const fakeFile = new File(['(ˆ_ˆ)'], 'avatar.png', { type: 'image/png' });

        // trigger the file change handler with a fake event containing the fake file
        result.current.handleFileChange({
            target: {
                files: [fakeFile] // Der Hook liest e.target.files?.[0]
            }
        } as any);

        await waitFor(() => {
            expect(result.current.formData.profilePicture).toBe(expectedBase64);
            expect(result.current.error).toBe(''); // No Errors
        });
    });

    it('should set an error when image processing fails', async () => {
        const mockProfile = { firstName: 'Max', lastName: 'Mustermann', matriculationNumber: '' };
        vi.mocked(userApi.getProfile).mockResolvedValue(mockProfile as any);

        // simulate error in fileUtils
        vi.mocked(convertFileToBase64).mockRejectedValue(new Error('Crash!'));

        const { result } = renderHook(() => useProfileEdit());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        const fakeFile = new File([''], 'broken.png', { type: 'image/png' });

        // Trigger
        result.current.handleFileChange({
            target: { files: [fakeFile] }
        } as any);

        await waitFor(() => {
            expect(result.current.error).toBe('Bildverarbeitung fehlgeschlagen.');
            // profile pciture should not be updated on error
            expect(result.current.formData.profilePicture).toBe(''); 
        });
    });

    it('should submit the form and navigate to profile', async () => {
        const mockProfile = { firstName: 'Max', lastName: 'Mustermann', matriculationNumber: '' };
        vi.mocked(userApi.getProfile).mockResolvedValue(mockProfile as any);

        vi.mocked(userApi.updateProfile).mockResolvedValue({} as any);

        const { result } = renderHook(() => useProfileEdit());

        await waitFor(() => { expect(result.current.loading).toBe(false); });

        result.current.handleSubmit({ preventDefault: vi.fn() } as any);

        await waitFor(() => {
            expect(userApi.updateProfile).toHaveBeenCalledWith(result.current.formData);
            expect(mockNavigate).toHaveBeenCalledWith('/profile');
            expect(result.current.error).toBe('');
        });
    });

    it('should set an error when the API update fails', async () => {
        const mockProfile = { firstName: 'Max', lastName: 'Mustermann', matriculationNumber: '' };
        vi.mocked(userApi.getProfile).mockResolvedValue(mockProfile as any);

        vi.mocked(userApi.updateProfile).mockRejectedValue(new Error('Server Error'));

        const { result } = renderHook(() => useProfileEdit());

        await waitFor(() => { expect(result.current.loading).toBe(false); });

        result.current.handleSubmit({ preventDefault: vi.fn() } as any);

        await waitFor(() => {
            expect(result.current.error).toBe('Update fehlgeschlagen.');
            expect(mockNavigate).not.toHaveBeenCalled();
        });
    });
});