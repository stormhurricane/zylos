import { renderHook } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { useNavbar } from './useNavbar';
import * as AuthModule from '../context/AuthContext';
import { BrowserRouter } from 'react-router-dom';
import React, { act } from 'react';

const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
    const actual = await vi.importActual('react-router-dom');
    return {
        ...actual as any,
        useNavigate: () => mockNavigate,
    };
});

describe('useNavbar Hook', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    const wrapper = ({ children }: { children: React.ReactNode }) => (
        <BrowserRouter>{children}</BrowserRouter>
    );

    it('should return default states based on auth context', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: { userId: 1, firstName: 'Prof', lastName: 'X' },
            logout: vi.fn(),
            isInstructor: true,
            loading: false,
            isAuthenticated: true
        } as any);

        const { result } = renderHook(() => useNavbar(), { wrapper });

        expect(result.current.isInstructor).toBe(true);
        expect(result.current.searchQuery).toBe('');
    });

    it('should update searchQuery state when setSearchQuery is called', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: null, logout: vi.fn(), isInstructor: false, loading: false, isAuthenticated: false
        } as any);

        const { result } = renderHook(() => useNavbar(), { wrapper });

        act(() => {
            result.current.setSearchQuery('React Kurs');
        });

        expect(result.current.searchQuery).toBe('React Kurs');
    });

    it('should navigate to dashboard with encoded query on search submit', () => {
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: null, logout: vi.fn(), isInstructor: false, loading: false, isAuthenticated: false
        } as any);

        const { result } = renderHook(() => useNavbar(), { wrapper });

        act(() => {
            result.current.setSearchQuery('Vue & React');
        });

        const mockEvent = { preventDefault: vi.fn() } as unknown as React.FormEvent;
        
        act(() => {
            result.current.handleSearchSubmit(mockEvent);
        });

        expect(mockEvent.preventDefault).toHaveBeenCalled();
        
        expect(mockNavigate).toHaveBeenCalledWith('/dashboard?q=Vue%20%26%20React');
    });

    it('should call logout from auth context when hook logout is triggered', () => {
        const mockLogoutApi = vi.fn();
        vi.spyOn(AuthModule, 'useAuth').mockReturnValue({
            user: null, logout: mockLogoutApi, isInstructor: false, loading: false, isAuthenticated: false
        } as any);

        const { result } = renderHook(() => useNavbar(), { wrapper });

        act(() => {
            result.current.logout();
        });

        expect(mockLogoutApi).toHaveBeenCalledTimes(1);
    });
});