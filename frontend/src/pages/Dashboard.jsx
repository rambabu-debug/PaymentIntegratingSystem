import { useState, useEffect } from 'react'
import { paymentAPI } from '../services/api'

export default function Dashboard() {
  const [user, setUser] = useState(null)
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState({
    totalTransactions: 0,
    totalAmount: 0,
    successfulTransactions: 0,
  })

  useEffect(() => {
    loadDashboardData()
  }, [])

  const loadDashboardData = async () => {
    try {
      setLoading(true)
      const userEmail = localStorage.getItem('userEmail')
      const userRole = localStorage.getItem('userRole')

      setUser({
        email: userEmail,
        role: userRole,
      })

      // Try to load transactions
      try {
        const response = await paymentAPI.getTransactions()
        const txns = response.data || []
        setTransactions(txns)

        // Calculate stats
        const total = txns.length
        const successful = txns.filter((t) => t.status === 'succeeded').length
        const amount = txns.reduce((sum, t) => sum + (t.amount || 0), 0)

        setStats({
          totalTransactions: total,
          totalAmount: (amount / 100).toFixed(2),
          successfulTransactions: successful,
        })
      } catch (err) {
        // Transactions endpoint might not exist yet
        console.log('No transactions yet')
        setTransactions([])
      }
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('userEmail')
    localStorage.removeItem('userRole')
    window.location.href = '/'
  }

  if (loading) {
    return (
      <div className="dashboard">
        <div className="loading">
          <div className="spinner"></div>
          Loading dashboard...
        </div>
      </div>
    )
  }

  return (
    <div className="dashboard">
      <div className="navbar">
        <h2>💰 Payment Merchant Dashboard</h2>
        <div className="navbar-user">
          <span>{user?.email}</span>
          <button onClick={handleLogout}>Logout</button>
        </div>
      </div>

      <div className="container">
        {/* Stats Cards */}
        <div className="grid">
          <div className="card">
            <h3>Total Transactions</h3>
            <div className="card-value">{stats.totalTransactions}</div>
            <div className="card-subtitle">All time</div>
          </div>

          <div className="card">
            <h3>Total Amount</h3>
            <div className="card-value">${stats.totalAmount}</div>
            <div className="card-subtitle">USD</div>
          </div>

          <div className="card">
            <h3>Successful</h3>
            <div className="card-value">{stats.successfulTransactions}</div>
            <div className="card-subtitle">Transactions</div>
          </div>
        </div>

        {/* Transactions Table */}
        <div className="card">
          <h2 style={{ marginBottom: '20px', color: '#667eea' }}>
            Recent Transactions
          </h2>
          {transactions.length > 0 ? (
            <div className="table-container">
              <table>
                <thead>
                  <tr>
                    <th>Transaction ID</th>
                    <th>Amount</th>
                    <th>Currency</th>
                    <th>Status</th>
                    <th>Date</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.map((tx) => (
                    <tr key={tx.id}>
                      <td>{tx.stripeTransactionId || tx.id}</td>
                      <td>${(tx.amount / 100).toFixed(2)}</td>
                      <td>{tx.currency}</td>
                      <td>
                        <span
                          className={`status-badge status-${tx.status === 'succeeded' ? 'success' : tx.status === 'failed' ? 'failed' : 'pending'}`}
                        >
                          {tx.status}
                        </span>
                      </td>
                      <td>{new Date(tx.createdAt).toLocaleDateString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p style={{ color: '#999', textAlign: 'center', padding: '20px' }}>
              No transactions yet
            </p>
          )}
        </div>

        {/* User Role Info */}
        <div className="card" style={{ marginTop: '20px' }}>
          <h3>Account Information</h3>
          <div style={{ marginTop: '15px' }}>
            <div className="info-row">
              <span className="info-label">Email:</span>
              <span className="info-value">{user?.email}</span>
            </div>
            <div className="info-row">
              <span className="info-label">Account Type:</span>
              <span className="info-value">{user?.role?.toUpperCase()}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

