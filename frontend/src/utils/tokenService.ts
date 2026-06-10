// In Memory var for safety
let _accessToken: string | null = null;

export const tokenService = {
    setToken: (token: string) => {
        _accessToken = token;
        
        // Temporary Fix for F5 Refresh until Spring Boot provides HttpOnly cookies
        sessionStorage.setItem('authToken', token);
    },
    
    getToken: (): string | null => {
        if (_accessToken) return _accessToken;
        
        _accessToken = sessionStorage.getItem('authToken');
        return _accessToken;
    },
    
    clearToken: () => {
        _accessToken = null;
        sessionStorage.removeItem('authToken');
    }
};