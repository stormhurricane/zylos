import { ApiError } from "./apiErrors";

const BASE_URL = import.meta.env.API_BASE_URL || "http://localhost:8080/api";

export async function apiClient<T>(
    endpoint: string,
    options: RequestInit = {}
): Promise<T> {
    // build URL for Request
    const url = `${BASE_URL}${endpoint.startsWith('/') ? endpoint : `/${endpoint}`}`;

    // set standard header (e.g. Content-Type)
    const headers: HeadersInit = {
        "Content-Type": "application/json",
        ...options.headers,
    };
    
    const response = await fetch(url, {
        ...options,
        headers
    });

    if(!response.ok) {
        const errorData: ApiError = await response.json().catch(() => {});

        throw new ApiError({
            ... errorData,
            status: errorData.status ?? response.status,
        });
    }

    if(response.status == 204) {
        return {} as T;
    }

    return response.json() as Promise<T>;
}