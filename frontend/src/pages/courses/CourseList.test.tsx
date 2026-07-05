import { screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom/vitest';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { CourseList } from './CourseList';
import { http, HttpResponse } from 'msw';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { TEST_BASE_URL, mockCoursesData } from '../../test/handlers';
import { act } from 'react';
import { server } from '../../test/server';

const renderCourseList = () => {
    return renderWithAuthAndRouter(<CourseList/>);
};



describe('CourseList Component', () => {

    beforeEach(() => {
        window.alert = vi.fn();
    });


    it('should render courses and enroll in first', async () => {
        const user = userEvent.setup();
        renderCourseList();

        expect(await screen.findByRole("link", {name: "Software Engineering"}));
        expect(screen.getByRole("link", {name: "Database Systems"}));


        const enrollButtons = screen.getAllByRole('button', { name: /Einschreiben/i });
        await act(async () => await user.click(enrollButtons[0])); 

        await vi.waitFor(() => {
            expect(window.alert).toHaveBeenCalledWith('Erfolgreich eingeschrieben!');
        });
    });

    it('should show "Ansehen" Button, if already enrolled', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/courses/my-courses`, () => {
                return HttpResponse.json({ teachingCourses: [], enrolledCourses: [mockCoursesData[0]] });
            })
        );

        renderCourseList();

        expect(await screen.findByRole('button', { name: /Ansehen/i })).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /Einschreiben/i })).toBeInTheDocument();
    });

    it('should show empty if no courses returned by API', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/courses`, () => HttpResponse.json([])),
            http.get(`${TEST_BASE_URL}/courses/my-enrollments`, () => HttpResponse.json([]))
        );

        renderCourseList();

        expect(await screen.findByText(/Keine Lehrveranstaltungen gefunden/i)).toBeInTheDocument();
    });

    it('should show an error message', async () => {
        const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {});

        server.use(
            http.get(`${TEST_BASE_URL}/courses`, () => new HttpResponse(null, { status: 500 }))
        );

        renderCourseList();

        expect(await screen.findByText(/Fehler beim Laden der Kurse/i)).toBeInTheDocument();

        consoleSpy.mockRestore();
    });
});