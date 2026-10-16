import { RouterProvider } from 'react-router-dom';
import './App.css'
import router from './router';
import { AuthProvider } from './auth/context/AuthContext';

function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  )
}

export default App
