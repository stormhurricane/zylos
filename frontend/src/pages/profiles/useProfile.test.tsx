import { beforeEach, describe, expect, vi } from "vitest";
import { it } from "vitest";
import { userApi } from "../../api/userApi";
import { courseApi } from "../../api/courseApi";
import { useProfile } from "./useProfile";
import { renderHook, waitFor } from "@testing-library/react";

vi.mock('../../api/userApi', () => ({
    userApi: {
        getProfile: vi.fn(),
    },
}));

vi.mock('../../api/courseApi', () => ({
    courseApi: {
        getMyCourses: vi.fn(),
    },
}));

vi.mock('../../context/AuthContext', () => ({
    useAuth: () => ({
        user: { userId: '123', email: 'own@uni.de', role: 'STUDENT' }
    }),
}));

describe('useProfile Hook', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should fetch profile data and courses for own profile', async () => {
        // Prepare mocks
        const mockProfile = { firstName: 'Sascha', lastName: 'Mustermann', email: 'own@uni.de' };
        const mockCourses = [{ id: 'c1', title: 'Software Engineering', term: 'WS', academicYear: '2026' }];

        vi.mocked(userApi.getProfile).mockResolvedValue(mockProfile as any);
        vi.mocked(courseApi.getMyCourses).mockResolvedValue({ data: mockCourses } as any);

        // render Hook 
        const { result } = renderHook(() => useProfile('123'));

        // wait for loading to finish
        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        // assertions
        expect(result.current.profile).toEqual(mockProfile);
        expect(result.current.courses).toEqual(mockCourses);
        expect(result.current.isOwnProfile).toBe(true);
        expect(result.current.error).toBeNull();
        
        // assert that the API was called with correct parameters
        expect(userApi.getProfile).toHaveBeenCalledWith('123');
        expect(courseApi.getMyCourses).toHaveBeenCalled();
    });

    it('should fetch profile data but not courses for other profiles', async () => {
        const mockProfile = { firstName: 'Max', lastName: 'Mustermann', email: 'other@uni.de' };

        vi.mocked(userApi.getProfile).mockResolvedValue(mockProfile as any);

        const { result } = renderHook(() => useProfile('999')); // 999 != 123 => different profile
        // 999 ungleich 123 (eingeloggter User) -> fremdes Profil

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        expect(result.current.profile).toEqual(mockProfile);
        expect(result.current.isOwnProfile).toBe(false);
        expect(result.current.courses).toEqual([]); // courses should be empty for other profiles
        expect(result.current.error).toBeNull();

        expect(userApi.getProfile).toHaveBeenCalledWith('999');
        expect(courseApi.getMyCourses).not.toHaveBeenCalled(); // getMyCourses should not have been called for other profiles

    });

    it('should handle not found profile', async () => {
        vi.mocked(userApi.getProfile).mockResolvedValue(null);        
        const { result } = renderHook(() => useProfile('404'));

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        expect(result.current.profile).toBeNull();
        expect(result.current.isOwnProfile).toBe(false);
        expect(result.current.courses).toEqual([]);
        expect(result.current.error).toBeNull();        
        
        expect(userApi.getProfile).toHaveBeenCalledWith('404');
        expect(courseApi.getMyCourses).not.toHaveBeenCalled();
    });

    it('should handle API errors', async () => {
        vi.mocked(userApi.getProfile).mockRejectedValue(new Error('Internal Server Error'));

        const { result } = renderHook(() => useProfile('error'));

        await waitFor(() => {
            expect(result.current.loading).toBe(false);
        });

        expect(result.current.profile).toBeNull();
        expect(result.current.error).toBe('Das Profil konnte aufgrund eines Serverfehlers nicht geladen werden.');
        expect(result.current.courses).toEqual([]);
        
        expect(userApi.getProfile).toHaveBeenCalledWith('error');
    });

});