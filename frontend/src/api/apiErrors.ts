import { type ErrorResponse } from "../types";

export class ApiError extends Error{
    readonly status: number;
    readonly timestamp: string;
    readonly errors?: Record<string, string>;

    constructor(data: ErrorResponse){
        super(data.message || 'An unexpecte API Error occured');
        this.name = 'API Error';
        this.status = data.status ?? 500;
        this.timestamp = data.timestamp ?? new Date().toISOString();
        this.errors = data.errors;

        Object.setPrototypeOf(this, ApiError.prototype);
    }
}