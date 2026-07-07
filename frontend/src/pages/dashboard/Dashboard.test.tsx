import { screen } from '@testing-library/react';
import { describe, it, expect} from 'vitest';
import { Dashboard } from './Dashboard';
import { http, HttpResponse } from 'msw';
import { mockCoursesData, TEST_BASE_URL } from '../../test/handlers';
import { renderWithAuthAndRouter } from '../../test/testUtils';
import { server } from '../../test/server';

describe('Dashboard Component Integration', () => {
    
    it('should render courses of the user and show them sorted', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/courses/my-courses`, () => {
                    return HttpResponse.json({ teachingCourses: [], enrolledCourses: mockCoursesData });
                }),
        );

        renderWithAuthAndRouter(<Dashboard/>);

        expect(screen.getByText('Kurse werden geladen...')).toBeInTheDocument();

        const firstCourse = await screen.findByText(mockCoursesData[0].title);
        const secondCourse = screen.getByText(mockCoursesData[1].title);

        expect(firstCourse).toBeInTheDocument();
        expect(secondCourse).toBeInTheDocument();
        
        expect(firstCourse.compareDocumentPosition(secondCourse)).toBe(Node.DOCUMENT_POSITION_PRECEDING);
    });

    it('should show an error if user search fails', async () => {
        server.use(
            http.get(`${TEST_BASE_URL}/users/search`, () => {
                return new HttpResponse(null, { status: 500 });
            })
        );

        renderWithAuthAndRouter(<Dashboard />, ['/dashboard?q=UnbekannterUser']);

        const errorAlert = await screen.findByText(/Die Nutzersuche ist fehlgeschlagen\. Bitte erneut versuchen\./i);
        expect(errorAlert).toBeInTheDocument();
    });
});