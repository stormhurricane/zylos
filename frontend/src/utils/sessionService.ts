import { AuthResponse } from '../api/types';

type UserData = Omit<AuthResponse, 'accessToken'>;

// In Memory var for safety
let _accessToken: string | null = null;
let _user: UserData | null = null;

export const sessionService = {
    // Kombinierte Speicheroperation: Alles an einem sicheren Ort
    saveSession: (token: string, user: UserData) => {
        _accessToken = token;
        _user = user;
        
        sessionStorage.setItem('authToken', token);
        sessionStorage.setItem('authUser', JSON.stringify(user));
    },
    
    getToken: (): string | null => {
        if (_accessToken) return _accessToken;
        
        _accessToken = sessionStorage.getItem('authToken');
        return _accessToken;
    },

    getSavedUser: (): UserData | null => {
        if (_user) return _user;
        
        const savedUserStr = sessionStorage.getItem('authUser');
        if (!savedUserStr) return null;

        try {
            _user = JSON.parse(savedUserStr);
            return _user;
        } catch (error) {
            console.error('Fehler beim Parsen der Usersession im tokenService:', error);
            sessionService.clearSession();
            return null;
        }
    },
    
    // Kill-Switch for Axios-Interceptor at 401 or manual logout
    clearSession: () => {
        _accessToken = null;
        _user = null;
        sessionStorage.removeItem('authToken');
        sessionStorage.removeItem('authUser');
    }
};