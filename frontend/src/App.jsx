import { useState, useEffect } from 'react'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'

export default function App() {
  const [currentPage, setCurrentPage] = useState(() => {
    const token = localStorage.getItem('token')
    return token ? 'dashboard' : 'login'
  })

  // Listen for storage changes (token updates from other tabs/components)
  useEffect(() => {
    const handleStorageChange = () => {
      const token = localStorage.getItem('token')
      if (token) {
        setCurrentPage('dashboard')
      } else {
        setCurrentPage('login')
      }
    }

    window.addEventListener('storage', handleStorageChange)
    return () => window.removeEventListener('storage', handleStorageChange)
  }, [])

  const handleSwitchToLogin = () => setCurrentPage('login')
  const handleSwitchToRegister = () => setCurrentPage('register')
  const handleLoginSuccess = () => setCurrentPage('dashboard')

  if (currentPage === 'dashboard') {
    const token = localStorage.getItem('token')
    if (!token) {
      setCurrentPage('login')
      return null
    }
    return <Dashboard />
  }

  if (currentPage === 'register') {
    return <Register onSwitchToLogin={handleSwitchToLogin} onSuccess={handleLoginSuccess} />
  }

  return <Login onSwitchToRegister={handleSwitchToRegister} onSuccess={handleLoginSuccess} />
}

