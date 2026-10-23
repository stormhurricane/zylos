import { vi, beforeEach, describe, it, expect } from "vitest";
import { render, waitFor, screen, fireEvent } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Routes, Route, type InitialEntry } from "react-router-dom";
import { Login } from "./Login";
import { AuthProvider } from "../context/AuthProvider";
import { toast } from 'sonner';
import { server } from "../../test/setup";
import { http, HttpResponse } from "msw";
import { type UserResponse, type ErrorResponse } from "../../types";


vi.mock('sonner', () => ({
    toast: { success: vi.fn(), error: vi.fn() }
}));

const createTestQueryClient = () => new QueryClient({
    defaultOptions: {
        queries: { retry: false, gcTime: Infinity }, 
        mutations: { retry: false }
    }
});

const renderLogin = (initialEntries: InitialEntry[] = ["/login"]) => {
    const user = userEvent.setup();
    const queryClient = createTestQueryClient();

    return {
        user,
        ...render(
            <QueryClientProvider client = {queryClient}>
                <MemoryRouter initialEntries={initialEntries}>
                    <AuthProvider>
                        <Routes>
                            <Route path="/login" element={<Login/>}/>
                            <Route path="/home" element={<div>Dashboard Page</div>} />
                            <Route path="/settings" element={<div>Settings Page</div>} />
                        </Routes>
                    </AuthProvider>
                </MemoryRouter>
            </QueryClientProvider>
        ),
    }
};

const fillLoginFormFast = (identifier: string = "testUser", password: string = "password") => {
    fireEvent.change(screen.getByRole('textbox', { name: /identifier/i }), { target: { value: identifier } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: password } });
};

beforeEach(() => {
    vi.clearAllMocks();
});

const mockNotLoggedIn = () => {
    server.use(
        http.get('**/api/v1/auth/me',
            () => {
                const errorResponse: ErrorResponse = {
                    status: 401,
                    message: "Unauthorized",
                    timestamp: new Date().toISOString()
                }
                return HttpResponse.json(errorResponse, { status: 401 });
            }
        )
    );
};


describe("Login Component", () => {

    describe("Initial Rendering & Authentication Guards", () => {
        it("should render form correctly with empty fields", () => {
            const { getByRole, getByLabelText } = renderLogin();
    
            expect(getByRole('textbox', { name: /identifier/i})).toHaveValue("");
            expect(getByLabelText(/password/i)).toHaveValue(""); 
        });
    
        it("should redirect to /dashboard if user is already authenticated", async () => {
            renderLogin();
            expect(await screen.findByText("Dashboard Page")).toBeInTheDocument();
        });
    });


    describe("Client Form Validation", () => {
        it("should show validation errors when leaving fields empty (onBlur)", async () => {
            mockNotLoggedIn();
            const { getByRole, getByLabelText, user } = renderLogin();

            const identifierInput = getByRole("textbox", { name: /identifier/i });
            const passwordInput = getByLabelText(/password/i);

            await user.click(identifierInput);
            await user.click(passwordInput);
            await user.click(document.body);

            expect(await screen.findByText(/enter your username or email/i)).toBeInTheDocument();
            expect(await screen.findByText(/passwords must be at least 6 characters long/i)).toBeInTheDocument();
        });

        it("should not submit form when fields are invalid", async () => {
            mockNotLoggedIn();
            const { user, getByRole, findAllByRole } = renderLogin();
            
            const submitButton = getByRole('button', { name: /login/i });
            await user.click(submitButton);

            const errorAlerts = await findAllByRole("alert");

            expect(errorAlerts.length).toBeGreaterThan(0);
            expect(await screen.findByText(/enter your username or email/i)).toBeInTheDocument();
        });
    });


    describe("Form Submission & Navigation", () => {
        it("should show pending state while login mutation is running", async () => {
            let resolveLogin!: () => void
            const submitPromise = new Promise<void>((resolve) => {
                resolveLogin = resolve;
            });
    
            mockNotLoggedIn();
            server.use(
                http.post("**/api/v1/auth/login", async () => {
                    await submitPromise;
                    const userResponse: UserResponse = {
                        id: "uniqID",
                        username: "testUser",
                        email: "test@example.com",
                        roles: ["STUDENT"],
                    }
                    return HttpResponse.json(userResponse, { status: 200 });
                })
            )
    
            renderLogin();
            fillLoginFormFast("testUser", "password");
    
            const submitButton = screen.getByRole('button', { name: /login/i });
            fireEvent.click(submitButton);
    
            await waitFor(() => {
                expect(submitButton).toBeDisabled();
                expect(submitButton).toHaveTextContent("Logging in...");
            })
    
            resolveLogin();
        });
    
        it("should submit valid data and navigate to /dashboard", async () => {
            mockNotLoggedIn();
    
            renderLogin();
            fillLoginFormFast("testUser", "password");
    
            const submitButton = screen.getByRole('button', { name: /login/i});
            fireEvent.click(submitButton);
    
            await waitFor(() => {
                expect(toast.success).toHaveBeenCalledWith("Login successful");
            });
            expect(await screen.findByText("Dashboard Page")).toBeInTheDocument();
        });
        
        it("should navigate to custom origin URL if state.from is provided", async () => {
            mockNotLoggedIn();

            renderLogin([{ pathname: "/login", state: { from: { pathname: "/settings" }}}]);
            fillLoginFormFast("testUser", "password");

            const submitButton = screen.getByRole('button', { name: /login/i});
            fireEvent.click(submitButton);

            expect(await screen.findByText("Settings Page")).toBeInTheDocument();
        });
    });

    describe("Backend Error Handling", () => {

        it("should handle 401 Unauthorized backend error generically", async () => {
            mockNotLoggedIn();

            server.use(
                http.post("**/api/v1/auth/login", () => {
                    const errorResponse: ErrorResponse = {
                        status: 401,
                        message: "Invalid identifier or password",
                        timestamp: new Date().toISOString()
                    };
                    return HttpResponse.json(errorResponse, { status: 401 });
                })
            );

            const { findByRole } = renderLogin([{ pathname: "/login", state: { from: { pathname: "/settings" }}}]);
            fillLoginFormFast("testUser", "password");
    
            const submitButton = screen.getByRole('button', { name: /login/i});
            fireEvent.click(submitButton);

            const alertElement = await findByRole('alert');
            expect(alertElement).toBeInTheDocument();
            expect(alertElement).toHaveTextContent(/invalid identifier or password/i);

            expect(screen.queryByText(/user not found/i)).not.toBeInTheDocument();
            
        });
    
        it("should handle 500 Internal Server Error gracefully", async () => {
            mockNotLoggedIn();
            server.use(
                http.post("**/api/v1/auth/login", () => {
                    const errorResponse: ErrorResponse = {
                        status: 500,
                        message: "Unexpected error occured",
                        timestamp: new Date().toISOString()
                    };
                    return HttpResponse.json(errorResponse, { status: 500 });
                })
            );

            const { findByRole } = renderLogin([{ pathname: "/login", state: { from: { pathname: "/settings" }}}]);
            fillLoginFormFast("testUser", "password");

            const submitButton = screen.getByRole('button', { name: /login/i});
            fireEvent.click(submitButton);

            const alertElement = await findByRole('alert');
            expect(alertElement).toBeInTheDocument();
            expect(alertElement).toHaveTextContent(/unexpected error occured/i);
        });
    });

});