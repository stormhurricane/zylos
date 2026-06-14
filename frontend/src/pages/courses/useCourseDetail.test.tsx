import { renderHook, waitFor, screen } from '@testing-library/react';
import { act } from 'react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useCourseDetail } from './useCourseDetail';
import { courseApi } from '../../api/courseApi';
import { userApi } from '../../api/userApi';
import * as fileUtils from '../../utils/fileUtils';
vi.mock('../../utils/downloadUtils', () => ({
    triggerBinaryDownload: vi.fn() // <--- Komplett weggemockt!
}));

vi.mock('../../api/courseApi', () => ({
    courseApi: {
        getCourseById: vi.fn(),
        getParticipants: vi.fn(),
        getMaterials: vi.fn(),
        addParticipant: vi.fn(),
        uploadMaterial: vi.fn(),
        downloadMaterial: vi.fn()
    }
}));

vi.mock('../../api/userApi', () => ({
    userApi: {
        searchUsers: vi.fn()
    }
}));

// mock react router params
vi.mock('react-router-dom', () => ({
    useParams: () => ({ id: '123' })
}));


// mock auth context
const mockAuth = { user: { userId: 1 }, isInstructor: true };
vi.mock('../../context/AuthContext', () => ({
    useAuth: () => mockAuth
}));

describe('useCourseDetail Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        // mock global window.URL for download test
        window.URL.createObjectURL = vi.fn(() => 'blob:mock-url');
        window.URL.revokeObjectURL = vi.fn();
    });

    it('should load course data, participants, and materials', async () => {
        const mockCourseData = {
            id: 123,
            title: 'Software Engineering',
            type: 'LECTURE',
            term: 'WINTER',
            academicYear: '2023/24'
        };

        vi.mocked(courseApi.getCourseById).mockResolvedValue( mockCourseData as any);
        
        vi.mocked(courseApi.getParticipants).mockResolvedValue({
             instructors: [{ id: 1, firstName: 'Prof.', lastName: 'Tester' }], students: [] 
        } as any);
        
        vi.mocked(courseApi.getMaterials).mockResolvedValue([
            { id: 1, title: 'Skript 1', fileName: 'skript1.pdf' }
        ] as any);

        // Hook
        const { result } = renderHook(() => useCourseDetail());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        // Assertions:
        expect(result.current.course).toEqual(mockCourseData);
        expect(result.current.materials).toHaveLength(1);
        expect(result.current.participants?.instructors).toHaveLength(1);

    });

    it('should filter user search results correctly', async () => {
        vi.mocked(courseApi.getCourseById).mockResolvedValue({ id: 123 } as any);
        vi.mocked(courseApi.getParticipants).mockResolvedValue({
            instructors: [], students: [{ id: 99, firstName: 'Existiert', lastName: 'Schon' }]
        } as any);
        vi.mocked(courseApi.getMaterials).mockResolvedValue( [] as any);

        vi.mocked(userApi.searchUsers).mockResolvedValue([
                { id: 99, firstName: 'Existiert', lastName: 'Schon' },
                { id: 100, firstName: 'Neuer', lastName: 'Student' }
        ] as any);

        const { result } = renderHook(() => useCourseDetail());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        // 3. Suche nackt triggern
        act(() => {
            result.current.handleStudentSearch('Max');
        });

        await waitFor(() => {
            expect(result.current.searchResults).toHaveLength(1);
            expect(result.current.searchResults[0].id).toBe(100); // Nur der neue Student darf übrig bleiben
        });
    });

    it('should upload a material successfully and update the data afterwards', async () => {
        vi.mocked(courseApi.getCourseById).mockResolvedValue({ id: 123 } as any);
        vi.mocked(courseApi.getParticipants).mockResolvedValue( { instructors: [], students: [] } as any);
        vi.mocked(courseApi.getMaterials).mockResolvedValue( [] as any);

        vi.mocked(courseApi.uploadMaterial).mockResolvedValue({ data: {} } as any);

        const { result } = renderHook(() => useCourseDetail());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        act(() => {
            result.current.setUploadTitle('Vorlesung 1');
            const fakeFile = new File(['content'], 'vorlesung1.pdf', { type: 'application/pdf' });
            result.current.setUploadFile(fakeFile);
        });

        act(() => {
            result.current.handleUpload({ preventDefault: vi.fn() } as any);
        });

        await waitFor(() => {
            expect(result.current.uploadStatus).toEqual({
                type: 'success',
                text: 'Material erfolgreich hochgeladen!'
            });
            expect(result.current.fileInputKey).toBe(1);
            expect(result.current.uploadTitle).toBe('');
        });
    });

    it('should call API on download and trigger file utils', async () => {
        vi.mocked(courseApi.getCourseById).mockResolvedValue( { id: 123 } as any);
        vi.mocked(courseApi.getParticipants).mockResolvedValue( { instructors: [], students: [] } as any);
        vi.mocked(courseApi.getMaterials).mockResolvedValue( [] as any);

        const fakeBlob = new Blob(['pdf-daten'], { type: 'application/pdf' });
        vi.mocked(courseApi.downloadMaterial).mockResolvedValue({ data: fakeBlob } as any);

        // Spy directly on module object and mock implementation to prevent actual DOM manipulation
        const downloadSpy = vi.spyOn(fileUtils, 'triggerBinaryDownload')
            .mockImplementation(() => {}); 

        const { result } = renderHook(() => useCourseDetail());

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        // trigger download
        result.current.handleDownload(456, 'skript.pdf');

        await waitFor(() => {
            expect(courseApi.downloadMaterial).toHaveBeenCalledWith(456);
            
            expect(downloadSpy).toHaveBeenCalledWith(
                expect.any(Blob), 
                'skript.pdf'
            );
        });

        downloadSpy.mockRestore();
    });

});