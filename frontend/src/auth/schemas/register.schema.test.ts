import { describe, expect, it } from "vitest";

import { registerSchema, type RegisterFormData } from "./register.schema";

const buildRegisterFormData = (overrides?: Partial<RegisterFormData>): RegisterFormData => ({
    firstName: "John",
    lastName: "Doe",
    email: "john.doe@example.com",
    username: "johndoe",
    password: "securePassword123",
    emailCopy: "john.doe@example.com",
    role: "STUDENT" as const,

    ...overrides
});

describe("Register Schema", () => {
    it("should validate a valid registration input", () => {
        const result = registerSchema.safeParse(buildRegisterFormData());
        expect(result.success).toBe(true);
    });

    describe("Field Validation", () => {
        it.each([
            {
                description: "invalid email format",
                overrides: { email: "invalidemail", emailCopy: "invalidemail" },
                expectedError: "Invalid email address"
            },
            {
                description: "empty emailCopy",
                overrides: { emailCopy: '' },
                expectedError: "Please confirm your email address"
            },
            {
                description: "too short firstName",
                overrides: { firstName: "Y" },
            },
            {
                description: "too short password",
                overrides: { password: "pass" },
            },
            {
                description: "wrong role enum",
                overrides: { role: "GUEST" as any },
            },
            {
                descripton: "too short username",
                overrides: { username: "ya" },
            }

        ])("should fail for $description", 
            ({ overrides, expectedError }) => {

                const result = registerSchema.safeParse(buildRegisterFormData(overrides));
                expect(result.success).toBe(false);
                if(!result.success && expectedError) {
                    const messages = result.error.issues.map((i) => i.message);
                    expect(messages).toContain(expectedError);
                }
        });

        describe("cross-field validation", () => {
            it("maps differing mail error to path ['emailCopy']", () => {
                const data = buildRegisterFormData({ emailCopy: "different@example.com" });
                const result = registerSchema.safeParse(data);

                expect(result.success).toBe(false);
                if(!result.success){
                    const issue = result.error.issues[0];
                    expect(issue.message).toBe("Email addresses do not match");
                    expect(issue.path).toEqual(["emailCopy"]);
                }
            });
        });
    });

});