import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook, waitFor } from '@testing-library/react';
import { useDashboard } from './useDashboard';
import { courseApi } from '../../api/courseApi';
import { userApi } from '../../api/userApi';
import { MemoryRouter } from 'react-router-dom';

// 1. Mocking of courseApi to control its behavior in tests
vi.mock('../../api/courseApi', () => ({
    courseApi: {
        getMyCourses: vi.fn()
    }
}));
vi.mock('../../api/userApi', () => ({
    userApi: {
        searchUsers: vi.fn()
    }
}));

describe('useDashboard Hook', () => {
    
    beforeEach(() => {
        vi.clearAllMocks();

        // Default mock behavior for courseApi.getMyCourses to return an empty array, can be overridden in specific tests
        vi.mocked(courseApi.getMyCourses).mockResolvedValue({ data: [] } as any); // No Courses for this test
    });

    it('should initialize with empty search results and load courses', async () => {
        //simulate API response for this specific test case
        const mockCourses = [
            { id: 1, title: 'Course A', term: 'WINTER', academicYear: '2025/2026' },
            { id: 2, title: 'Course B', term: 'SOMMER', academicYear: '2025/2026' }
        ];

        // Faking API response with attribute "data" that contains our courses array.
        vi.mocked(courseApi.getMyCourses).mockResolvedValue({
            data: mockCourses
        } as any);

        // Starting Hook in test environment with MemoryRouter to provide necessary context for useSearchParams
        const { result } = renderHook(() => useDashboard(), {
             wrapper: MemoryRouter 
        });

        // Due to asynchronous useEffect in the hook, we need to wait a bit until the hook has processed the data.
        await waitFor(() => {
            expect(result.current.isLoadingCourses).toBe(false);
        });

        // CHecking if the hook correctly initialized searchResults and loaded & sorted courses as expected!
        expect(result.current.searchResults).toHaveLength(0); // No Search Query = 0 hits

        expect(result.current.myCourses).toHaveLength(2); // 2 Courses loaded!
        expect(result.current.myCourses[0].title).toBe('Course A'); // Winter-Course on 1
    });

    it('should correctly handle a search query in URL', async () => {

        const mockSearchResults = [
            { id: 1, firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com' }
        ];

        vi.mocked(userApi.searchUsers).mockResolvedValue({
            data: mockSearchResults
        } as any);

        // Modified Wrapper to include initialEntries with search query "John"
        const { result } = renderHook(() => useDashboard(), {
            wrapper: ({ children }) => (
                <MemoryRouter initialEntries={['dashboard?q=John']}>
                    {children}
                </MemoryRouter>
            )
        });

        await waitFor(() => {
            expect(result.current.searchResults).toHaveLength(1);
        });


        expect(result.current.searchResults[0].firstName).toBe('John');    
    });

    it('should handle search API errors gracefully', async () => {
        vi.mocked(userApi.searchUsers).mockRejectedValue(new Error('API Error'));
        
        const { result } = renderHook(() => useDashboard(), {
            wrapper: ({ children }) => (
                <MemoryRouter initialEntries={['dashboard?q=FehlerTest']}>
                    {children}
                </MemoryRouter>
            )
        });

        await waitFor(() => {
            expect(result.current.searchError).toBe("Die Nutzersuche ist fehlgeschlagen. Bitte erneut versuchen.");
        });

        expect(result.current.searchResults).toHaveLength(0); // No results on error
    });

    it('should handle course loading errors gracefully', async () => {
        vi.mocked(courseApi.getMyCourses).mockRejectedValue(new Error('API Error')); // Simulate Course API failure

        const { result } = renderHook(() => useDashboard(), {
            wrapper: MemoryRouter
        });

        await waitFor(() => {
            expect(result.current.isLoadingCourses).toBe(false); // Loading should end even on error
        });

        expect(result.current.myCourses).toHaveLength(0); // No courses loaded on error
    });
});