import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, beforeAll, afterAll, beforeEach } from 'vitest';
import { setupServer } from 'msw/node';
import { http, HttpResponse } from 'msw';
import { CourseCreate } from './CourseCreate';
import { globalHandlers, TEST_BASE_URL } from '../../test/handlers';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { act } from 'react';
import { courseApi } from '../../api/courseApi';
import { CourseType, SemesterTerm } from '../../api/types';

const server = setupServer(...globalHandlers);
vi.mock('../../context/AuthContext', () => ({
    useAuth: () => ({
        user: { userId: 1, role: 'teacher' }, 
        isInstructor: true,
        loading: false
    }),
    AuthProvider: ({ children }: { children: React.ReactNode }) => <>{children}</>
}));

const renderCourseCreate = () => {
    return renderWithAuthAndRouter(<CourseCreate />);
};

describe('CourseCreate Component mit MSW', () => {

    beforeAll(() => server.listen({ onUnhandledRequest: 'error' })); 
    beforeEach(() => {
        server.resetHandlers()
        vi.clearAllMocks();
    }); 
    afterAll(() => server.close()); 

   it('should create a course manually', async () => {
        const user = userEvent.setup();
        renderCourseCreate();

        await user.type(screen.getByLabelText(/^Titel/i), 'Test Kurs MSW');
        await user.type(screen.getByLabelText(/^Jahr/i), '2026');
        
        await user.click(screen.getByRole('button', { name: /Erstellen/i }));

        screen.logTestingPlaygroundURL();

        expect(await screen.findByText('Lehrveranstaltung erfolgreich angelegt!')).toBeInTheDocument();
    });

    it('should show error message, if the server responds with an error', async () => {
        const user = userEvent.setup();

        server.use(
            http.post(`${TEST_BASE_URL}/courses`, () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderCourseCreate();

        await user.type(screen.getByLabelText(/^Titel/i), 'Fail Kurs');
        await user.type(screen.getByLabelText(/^Jahr/i), '2026');

        await user.click(screen.getByRole('button', { name: /Erstellen/i }));

        expect(await screen.findByText(/Fehler beim manuellen Anlegen/i)).toBeInTheDocument();
    });

    it('should import a csv file', async () => {
        const user = userEvent.setup();
        
       // JSDOM has bugs for FormData
        const importSpy = vi.spyOn(courseApi, 'importCsv').mockResolvedValue([
            { id: 201, title: 'Kurs 1', type: CourseType.LECTURE, term: SemesterTerm.SUMMER, academicYear: '2026' },
            { id: 202, title: 'Kurs 2', type: CourseType.SEMINAR, term: SemesterTerm.WINTER, academicYear: '2026' }
        ]);

        renderCourseCreate();

        const file = new File(['dummy-content'], 'courses.csv', { type: 'text/csv' });      
        const input = screen.getByText('', { selector: 'input[type="file"]' }) as HTMLInputElement;
        
        await act(async () => {
            await user.upload(input, file); 
    
            await user.click(screen.getByRole('button', { name: /CSV hochladen/i }));
        });

        expect(importSpy).toHaveBeenCalledWith(file);

        expect(await screen.findByText(/2 Lehrveranstaltungen erfolgreich importiert/i)).toBeInTheDocument();
        
        importSpy.mockRestore();
    
    });
});