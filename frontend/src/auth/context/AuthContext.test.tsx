import { describe, it, expect } from "vitest";
import { server } from "../../test/setup";
import { renderHook, waitFor } from "@testing-library/react";
import { AuthProvider } from "./AuthProvider";
import { useAuth } from "../hooks/useAuth";
import { http, HttpResponse } from "msw";
import type { ErrorResponse } from "../../types";

describe("AuthContext Test", () => {
    it("loads user data using 'me' endpoint", async () => {
        const { result } = renderHook(() => useAuth(), { wrapper: AuthProvider});

        await waitFor(() => {
            expect(result.current.isLoading).toBe(false);
        });

        expect(result.current.user).not.toBeNull();
        expect(result.current.user?.id).toBe("uniqueUIID");
        expect(result.current.user?.email).toBe("john.doe@example.com");
        expect(result.current.user?.username).toBe("johndoe");
        expect(result.current.user?.roles).toContain("STUDENT");
    });

    it("sets user to 'null' when '/me' returns 401", async () => {
        server.use(
            http.get('**/api/v1/auth/me', () => {
                const response: ErrorResponse = {
                    status: 401,
                    message: "Invalid login data or expired token",
                    timestamp: new Date().toISOString(),
                };
                return HttpResponse.json(response, { status: 401 });
            })
        );

        const { result } = renderHook(() => useAuth(), { wrapper: AuthProvider });
        await waitFor(() => {
            expect(result.current.isLoading).toBe(false);
        });

        expect(result.current.user).toBeNull();

    });
}); 
