import { http, HttpResponse } from 'msw';
import { Course } from '../api/types';

export const TEST_BASE_URL = 'http://localhost:8080/api';

export const mockCoursesData = [
    { id: 1, title: 'Software Engineering', type: 'LECTURE', term: 'SUMMER', academicYear: '2024' },
    { id: 2, title: 'Database Systems', type: 'SEMINAR', term: 'WINTER', academicYear: '2024' }
];

export const globalHandlers = [
    // Login-endpoint 
    http.post(`${TEST_BASE_URL}/users/login`, async ({ request }) => {
        return HttpResponse.json({ accessToken: 'mocked-jwt-token', role: 'STUDENT' });
    }),

    // Happy Path Registration
    http.post(`${TEST_BASE_URL}/users/register/student`, async () => {
        return HttpResponse.json({ success: true }, { status: 201 });
    }),

    http.post(`${TEST_BASE_URL}/users/register/teacher`, async () => {
        return HttpResponse.json({ success: true }, { status: 201 });
    }),

    // Global handler own profile
    http.get(`${TEST_BASE_URL}/users/me`, () => {
        return HttpResponse.json({
            id: 123, // Nativ als Number
            firstName: 'Jojen',
            lastName: 'Doe',
            email: 'own@uni.de',
            matriculationNumber: '1000001',
            studySubject: 'Informatik',
            privateAddress: 'Musterstraße 1'
        });
    }),

    http.put(`${TEST_BASE_URL}/users/me`, async ({ request }) => {
        const body = await request.json() as Record<string, any>;
        return HttpResponse.json({ success: true, ...body });
    }),

    http.get(`${TEST_BASE_URL}/users/:id`, ({ params }) => {
        const { id } = params;
        
        // 123 is own user
        if (id === '123' || id === 'me') {
            return HttpResponse.json({
                id: 123,
                firstName: 'Jojen',
                lastName: 'Doe',
                email: 'own@uni.de',
                matriculationNumber: '1000001',
                studySubject: 'Informatik',
                privateAddress: 'Musterstraße 1'
            });
        }

        return HttpResponse.json({
            id: Number(id),
            firstName: 'Fremder',
            lastName: 'Nutzer',
            email: 'stranger@uni.de',
            chair: 'Software Engineering',
            researchArea: 'KI & Ethik'
        });
    }),

    http.get(`${TEST_BASE_URL}/courses/my-enrollments`, () => {
        return HttpResponse.json([
            mockCoursesData[0]
        ]);
    }),

    http.get(`${TEST_BASE_URL}/courses/:id`, ({ params }) => {
        const { id } = params;
        
        if (id === 'undefined' || !id) {
            return new HttpResponse(null, { status: 404 });
        }

        return HttpResponse.json({
            id: Number(id), 
            title: `Generischer Kurs ${id}`,
            type: 'LECTURE',
            term: 'SUMMER',
            academicYear: '2026'
        });
    }),

    http.get(`${TEST_BASE_URL}/courses`, () => {
        return HttpResponse.json(mockCoursesData);
    }),

    http.post(`${TEST_BASE_URL}/courses/:id/enroll`, () => {
        return HttpResponse.json({ status: 200 });
    }),
    
    http.post(`${TEST_BASE_URL}/courses`, async ({ request }) => {
        const postData = await request.json() as Omit<Course, 'id'>;

        const newCourse = {
            id: 99,
            title: postData.title,
            type: postData.type,
            term: postData.term,
            academicYear: postData.academicYear,
        };

        return HttpResponse.json(newCourse);
    }),
];