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
import { Navbar } from './components/Navbar';
import { Outlet } from 'react-router-dom';

const MainLayout = () => (
  <div className="app-layout">
    <Navbar />
    <main>
      <Outlet />
    </main>
  </div>
);

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Protected Area with Layout */}
          <Route element={<ProtectedRoute><MainLayout /></ProtectedRoute>}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/profile" element={<Profile />} />
            <Route path="/profile/:id" element={<Profile />} />
            <Route path="/profile/edit" element={<ProfileEdit />} />
            
            <Route path="/courses" element={<CourseList />} />
            <Route 
              path="/courses/new" 
              element={<ProtectedRoute requiredRole="INSTRUCTOR"><CourseCreate /></ProtectedRoute>} 
            />
            <Route path="/courses/:id" element={<CourseDetail />} />
          </Route>

          {/* Standard-Rerouting */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
