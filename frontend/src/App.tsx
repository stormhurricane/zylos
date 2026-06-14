import React, { Suspense, lazy } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';
import { Navbar } from './components/Navbar';
import { PageLoader } from './components/PageLoader';
import { AuthGuardListener } from './context/AuthGuardListener';


// Lazy Loading
const Login = lazy(() => import('./pages/auth/Login').then(m => ({ default: m.Login })));
const Register = lazy(() => import('./pages/auth/Register').then(m => ({ default: m.Register })));
const Dashboard = lazy(() => import('./pages/dashboard/Dashboard').then(m => ({ default: m.Dashboard })));
const Profile = lazy(() => import('./pages/profiles/Profile').then(m => ({ default: m.Profile })));
const ProfileEdit = lazy(() => import('./pages/profiles/ProfileEdit').then(m => ({ default: m.ProfileEdit })));
const CourseList = lazy(() => import('./pages/courses/CourseList').then(m => ({ default: m.CourseList })));
const CourseCreate = lazy(() => import('./pages/courses/CourseCreate').then(m => ({ default: m.CourseCreate })));
const CourseDetail = lazy(() => import('./pages/courses/CourseDetail').then(m => ({ default: m.CourseDetail })));

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
        <AuthGuardListener />
        <Suspense fallback={<PageLoader />}>
          <Routes>
            {/* Public Routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* Protected Area with Layout */}
            <Route element={<ProtectedRoute><MainLayout /></ProtectedRoute>}>
              <Route path="/dashboard" element={<Dashboard />} />
              {/* Consolidated: A single path with an optional ID parameter would be possible here, 
                  but we'll stick to two explicit paths for better readability */}
              <Route path="/profile" element={<Profile />} />
              <Route path="/profile/:id" element={<Profile />} />
              <Route path="/profile/edit" element={<ProfileEdit />} />
              
              <Route path="/courses" element={<CourseList />} />
              <Route 
                path="/courses/new" 
                element={<ProtectedRoute requiredRole="TEACHER"><CourseCreate /></ProtectedRoute>} 
              />
              <Route path="/courses/:id" element={<CourseDetail />} />
            </Route>

            {/* Standard-Rerouting */}
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="*" element={<Navigate to="/dashboard" replace />} />
          </Routes>
        </Suspense>
      </Router>
    </AuthProvider>
  );
}

export default App;
