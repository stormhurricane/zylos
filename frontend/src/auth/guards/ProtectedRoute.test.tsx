import { describe, it, vi, expect } from "vitest";
import { useAuth } from "../hooks/useAuth";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import { ProtectedRoute } from "./ProtectedRoute";

vi.mock("../hooks/useAuth", () => ({
    useAuth: vi.fn(),
}));

describe("ProtectedRoute Test", () => {
    it("shows loading spinner during auth check", () => {
        vi.mocked(useAuth).mockReturnValue({
            user: null,
            isLoading: true,
            login: vi.fn(),
            logout: vi.fn()
        });

        render(
            <MemoryRouter initialEntries={["/dashboard"]}>
                <Routes>
                    <Route element={<ProtectedRoute/>}>
                        <Route path="/dashboard" element={<div>Dashboard</div>}/>
                    </Route>
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByText("Authentication is being verified...")).toBeInTheDocument();
        expect(screen.queryByText("Dashboard")).not.toBeInTheDocument();
    });

    it("should navigate unauthorized users to /login", () => {
        vi.mocked(useAuth).mockReturnValue({
            user: null,
            isLoading: false,
            login: vi.fn(),
            logout: vi.fn()
        });

        render(
            <MemoryRouter initialEntries={["/dashboard"]}>
                <Routes>
                    <Route path="/login" element={<div>Login</div>}/>
                    <Route element={<ProtectedRoute/>}>
                        <Route path="/dashboard" element={<div>Dashboard</div>}/>
                    </Route>
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByText("Login")).toBeInTheDocument();
        expect(screen.queryByText("Dashboard")).not.toBeInTheDocument();
    });

    it("shoudl render protected route for logged-in user", () => {
        vi.mocked(useAuth).mockReturnValue({
            user: {
                id: "unique-id",
                username: "username",
                email: "user@test.com",
                roles: ["STUDENT"]
            },
            isLoading: false,
            login: vi.fn(),
            logout: vi.fn()
        });

         render(
            <MemoryRouter initialEntries={["/dashboard"]}>
                <Routes>
                    <Route element={<ProtectedRoute/>}>
                        <Route path="/dashboard" element={<div>Dashboard</div>}/>
                    </Route>
                </Routes>
            </MemoryRouter>
        );

        expect(screen.getByText("Dashboard")).toBeInTheDocument();
    });
});
