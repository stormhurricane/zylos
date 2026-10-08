import { beforeEach, describe, it, vi, expect } from "vitest";
import { fireEvent,render, screen, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import userEvent from "@testing-library/user-event";
import { MemoryRouter } from "react-router-dom";
import { toast } from 'sonner';
import { http, HttpResponse } from "msw";
import { Register } from "./Register";
import { server } from "../../test/setup";
import { UserRole, type ErrorResponse } from "../../types";
import type { RegisterFormData } from "../schemas/register.schema";

// MOCKS

// mockNavigate as vi function
const mockNavigate = vi.fn();
// mock react-router package, but only replace useNavigate
vi.mock('react-router-dom', async () => ({
    ...(await vi.importActual('react-router-dom')),
    useNavigate: () => mockNavigate,
}));

vi.mock('sonner', () => ({
    toast: { success: vi.fn(), error: vi.fn() }
}));

const queryClient = new QueryClient({
    defaultOptions: {
        queries: { retry: false, gcTime: Infinity }, 
        mutations: { retry: false }
    }
});

// render function, queryClient needed for useMutation
const renderRegister = () => {
    const user = userEvent.setup();

    return {
        user,
        ...render(
            <QueryClientProvider client = {queryClient}>
                <MemoryRouter>
                    <Register />
                </MemoryRouter>
            </QueryClientProvider>
        ),
    }
};

const defaultValues: RegisterFormData = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    username: 'johndoe',
    emailCopy: 'john.doe@example.com',
    password: 'securePassword123',
    role: UserRole.STUDENT
}

const fillForm = async (
    user: ReturnType<typeof userEvent.setup>,
    getByRole: ReturnType<typeof renderRegister>['getByRole'],
    getByLabelText: ReturnType<typeof renderRegister>['getByLabelText'],
    overrides: Partial<RegisterFormData> = {},
    isFast: boolean = true,
) => {
    const data: Required<RegisterFormData> = {...defaultValues, ...overrides};

    const fields = [
        {element: getByRole('textbox', { name: /^email$/i }), value: data.email},
        {element: getByRole('textbox', { name: /repeat email/i }), value: data.emailCopy},
        {element: getByRole('textbox', { name: /first name/i }), value: data.firstName},
        {element: getByRole('textbox', { name: /last name/i }), value: data.lastName},
        {element: getByRole('textbox', { name: /username/i }), value: data.username},
        {element: getByLabelText(/password/i), value: data.password},
    ];

    for (const { element, value } of fields){
        if(!value) continue;

        if(isFast){
            fireEvent.change(element, { target: { value }});
        } else {
            await user.type(element, value);
        }
    }

    if (data.role === UserRole.TEACHER) {
        await user.click(getByRole('radio', { name: /teacher/i }));
    }
};

beforeEach(() => {
    vi.clearAllMocks();
    server.resetHandlers();
    queryClient.clear();
});

describe("Register Component Test", () => {

    it("should render formular correctly with empty fields and default role", () => {
        // getByRole is used to get aria labels, 
        // while getByLabelText gets the value of the associated label
        const { getByRole, getByLabelText } = renderRegister();

        expect(getByRole('textbox', { name: /first name/i})).toHaveValue("");
        expect(getByLabelText(/password/i)).toHaveValue("");
        expect(getByRole('radio', { name: /student/i })).toBeChecked();
        expect(getByRole('radio', { name: /teacher/i })).not.toBeChecked();
        expect(getByRole('button', { name: /register/i })).toBeEnabled();
        expect(getByRole('link', { name: /login/i })).toHaveAttribute('href', '/login');
    });

    // Happy Path
    it("should successfully submit registration, show loading state, toast, and navigate", async () => {
        // Deferred Promise
        let resolveSubmit!: () => void;
        const submitPromise = new Promise<void>((resolve) => {
            resolveSubmit = resolve;
        });

        // due to async behaviour, we have to override the standard handler
        server.use(
            http.post('**/api/v1/auth/register', async () => {
                await submitPromise;
                return HttpResponse.json(null, { status: 204 });
            })
        );

        const { user, getByRole, getByLabelText } = renderRegister();

        await fillForm(user, getByRole, getByLabelText);
        
        const submitButton = getByRole('button', { name: /register/i });
        
        await user.click(submitButton);
        await waitFor(() => {
            expect(submitButton).toBeDisabled();
        });

        resolveSubmit();

        await waitFor(() => {
            expect(toast.success).toHaveBeenCalledWith(
                expect.stringMatching('Success! Forwarding to login...')
            );
            expect(mockNavigate).toHaveBeenCalledWith('/login');
        });

    });

    describe('Client Form Validation', () => {
        it('should show validation errors on blur', async () => {
            const { user, getByRole, getByLabelText, findAllByRole } = renderRegister();

            await fillForm(user, getByRole, getByLabelText, { email: 'invalid-email'}, false);

            const emailInput = getByRole('textbox', { name: /^email$/i });
            await user.tab();
 
            const alerts = await findAllByRole('alert');
            expect(alerts[0]).toHaveTextContent(/invalid email address/i);
            expect(emailInput).toHaveAttribute('aria-invalid', 'true');
        });

        it('should not submit with client-side errors', async () => {
            const { user, getByRole, findAllByRole } = renderRegister();

            const submitButton = getByRole('button', { name: /register/i });
            await user.click(submitButton);

            const errorAlerts = await findAllByRole("alert");

            expect(errorAlerts.length).toBeGreaterThan(0);
            expect(await screen.findByText(/invalid email address/i)).toBeInTheDocument();
            expect(mockNavigate).not.toHaveBeenCalled();
        });
    });

    describe("Backend Error Handling", () => {
        it("should display field-specific errors from 400 response", async () => {
            server.use(
                http.post("**/api/v1/auth/register", () => {
                    return HttpResponse.json<ErrorResponse>({
                        status: 400,
                        message: 'Invalid Request body',
                        timestamp: new Date().toISOString(),
                        errors: {
                            email: 'Email should be valid'
                        },
                    }, { status: 400 });
                })
            );

            const { user, getByRole, getByLabelText } = renderRegister();
            await fillForm(user, getByRole, getByLabelText);

            const submitButton = getByRole('button', { name: /register/i });
            
            await user.click(submitButton);

            expect(await screen.findByText(/invalid request body/i)).toBeInTheDocument();

            expect(await screen.findByText(/email should be valid/i)).toBeInTheDocument();

            expect(getByRole('textbox', { name: /^email$/i })).toHaveAttribute('aria-invalid', 'true');
        });

        it("should handle 409 conflict error", async () => {
            server.use(
                http.post("**/api/v1/auth/register", () => {
                    return HttpResponse.json<ErrorResponse>({
                        status: 409,
                        message: 'Registration failed due to conflicting user data',
                        timestamp: new Date().toISOString(),
                        errors: {
                            username: 'Username is already in use'
                        },
                    }, { status: 409 });
                })
            );

            const { user, getByRole, getByLabelText } = renderRegister();
            await fillForm(user, getByRole, getByLabelText);

            const submitButton = getByRole('button', { name: /register/i });
            
            await user.click(submitButton);

            expect(await screen.findByText(/registration failed due to conflicting user data/i)).toBeInTheDocument();

            expect(await screen.findByText(/username is already in use/i)).toBeInTheDocument();

            expect(getByRole('textbox', { name: /^username$/i })).toHaveAttribute('aria-invalid', 'true');
        });

        it("should handle generic 500 server errors gracefully", async () => {
            server.use(
                http.post("**/api/v1/auth/register", () => {
                    return HttpResponse.json<ErrorResponse>({
                        status: 500,
                        message: 'An unexpected error occured',
                        timestamp: new Date().toISOString(),
                        errors: undefined
                    }, { status: 500 });
                })
            );

            const { user, getByRole, getByLabelText } = renderRegister();
            await fillForm(user, getByRole, getByLabelText);

            const submitButton = getByRole('button', { name: /register/i });
            
            await user.click(submitButton);

            expect(await screen.findByText(/an unexpected error occured/i)).toBeInTheDocument();
        });
    });

});
