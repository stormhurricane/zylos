import { describe, it, expect, vi, beforeEach } from 'vitest';
import { renderHook, waitFor, act } from '@testing-library/react';
import { userApi } from '../../api/userApi';
import { useRegister } from './useRegister';
import { MemoryRouter } from 'react-router-dom';

vi.mock('../../api/userApi', () => ({
    userApi: {
        registerStudent: vi.fn(),
        registerTeacher: vi.fn()
    }
}));

describe('useRegister Hook', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should abort registration if required fields are missing', async () => {
        const { result } = renderHook(() => useRegister(), {
            wrapper: MemoryRouter
        });

        const fakeEvent = { preventDefault: vi.fn() };

        await result.current.handleSubmit(fakeEvent as any);

        await waitFor(() => {
            expect(result.current.fieldErrors).toHaveProperty('firstName');
            expect(result.current.fieldErrors).toHaveProperty('lastName');
            expect(result.current.fieldErrors).toHaveProperty('email');
            expect(result.current.fieldErrors).toHaveProperty('password');
        });
    });

    it('should successfully register a student with cleaned payload', async () => {
        vi.mocked(userApi.registerStudent).mockResolvedValue({ data: {} } as any);

        const { result } = renderHook(() => useRegister(), {
            wrapper: MemoryRouter
        });

        // input data into form fields
        result.current.handleChange({ target: { name: 'firstName', value: 'Max' } } as any);
        result.current.handleChange({ target: { name: 'lastName', value: 'Mustermann' } } as any);
        result.current.handleChange({ target: { name: 'email', value: 'max@stud.uni.de' } } as any);
        result.current.handleChange({ target: { name: 'password', value: 'sicherespasswort123' } } as any);
        result.current.handleChange({ target: { name: 'studySubject', value: 'Informatik' } } as any);
        result.current.handleChange({ target: { name: 'chair', value: 'Müll-Daten' } } as any); // Soll gefiltert werden!

        // await filling data 
        await waitFor(() => {
            expect(result.current.formData.firstName).toBe('Max');
        });

        // trigger form submission
        const fakeEvent = { preventDefault: vi.fn() };
        result.current.handleSubmit(fakeEvent as any);

        // check cyclical until sucess is true and API was called with cleaned payload (chair should NOT be in payload for student)
        await waitFor(() => {
            console.log("AKTUELLE FEHLER IM HOOK:", result.current.fieldErrors);
            expect(result.current.success).toBe(true);
            
        });
        
        expect(userApi.registerStudent).toHaveBeenCalledWith({
            firstName: 'Max',
            lastName: 'Mustermann',
            email: 'max@stud.uni.de',
            password: 'sicherespasswort123',
            privateAddress: '',
            profilePicture: '',
            studySubject: 'Informatik'
            // 'chair' was filtered
        });
    });

    it('should successfully register a teacher with cleaned payload', async () => {
        vi.mocked(userApi.registerTeacher).mockResolvedValue({ data: {} } as any);

        const { result } = renderHook(() => useRegister(), {
            wrapper: MemoryRouter
        });

        result.current.setUserType('teacher');

        result.current.handleChange({ target: { name: 'firstName', value: 'Dr. Erika' } } as any);
        result.current.handleChange({ target: { name: 'lastName', value: 'Mustermann' } } as any);
        result.current.handleChange({ target: { name: 'email', value: 'erika.m@uni.de' } } as any);
        result.current.handleChange({ target: { name: 'password', value: 'lehrstuhlsicher!' } } as any);
        result.current.handleChange({ target: { name: 'chair', value: 'Distributed Systems' } } as any);
        result.current.handleChange({ target: { name: 'researchArea', value: 'Cloud Computing' } } as any);
        result.current.handleChange({ target: { name: 'studySubject', value: 'Schmuggel-Daten' } } as any); // should be filtered

        await waitFor(() => {
            expect(result.current.formData.chair).toBe('Distributed Systems');
        });

        const fakeEvent = { preventDefault: vi.fn() };
        result.current.handleSubmit(fakeEvent as any);

        // 5. Assertions
        await waitFor(() => {
            expect(result.current.success).toBe(true);
        });

        expect(userApi.registerTeacher).toHaveBeenCalledWith({
            firstName: 'Dr. Erika',
            lastName: 'Mustermann',
            email: 'erika.m@uni.de',
            password: 'lehrstuhlsicher!',
            privateAddress: '',
            profilePicture: '',
            chair: 'Distributed Systems',
            researchArea: 'Cloud Computing'
            // 'studySubject' wurde erfolgreich herausgefiltert!
        });
    });
    
});