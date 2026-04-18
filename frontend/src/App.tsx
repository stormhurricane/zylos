import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Dashboard } from './pages/Dashboard';
import { Profile } from './pages/Profile';
import { ProfileEdit } from './pages/ProfileEdit';
import { CourseList } from './pages/courses/CourseList';
import { CourseCreate } from './pages/courses/CourseCreate';
import { CourseDetail } from './pages/courses/CourseDetail';
import { ProtectedRoute } from './components/ProtectedRoute';

/**
 * Zentrales Routing der Zylos-Plattform.
 * Hier werden alle vertikalen Slices (Auth, Profile, Courses) zusammengeführt.
 */
function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Öffentliche Routen */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Dashboard & Profil */}
          <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
          <Route path="/profile/:id" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
          <Route path="/profile/edit" element={<ProtectedRoute><ProfileEdit /></ProtectedRoute>} />

          {/* Slice 2: Lehrveranstaltungen & Materialien */}
          {/* Übersicht aller Kurse */}
          <Route path="/courses" element={<ProtectedRoute><CourseList /></ProtectedRoute>} />
          
          {/* Erstellen einer neuen LV (Manuell/CSV) - Nur für Lehrende gedacht */}
          <Route path="/courses/new" element={<ProtectedRoute requiredRole="INSTRUCTOR"><CourseCreate /></ProtectedRoute>} />
          
          {/* Detailansicht mit Teilnehmerliste und Materialien */}
          <Route path="/courses/:id" element={<ProtectedRoute><CourseDetail /></ProtectedRoute>} />

          {/* Standard-Weiterleitung */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
