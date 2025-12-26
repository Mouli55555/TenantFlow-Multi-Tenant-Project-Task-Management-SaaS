import { Link } from "react-router-dom"
import { useAuth } from "../context/AuthContext"
import "../styles/navbar.css"

export default function Navbar() {
  const { user, logout } = useAuth()

  if (!user) return null

  const role = user.role

  return (
    <header className="navbar">
      <div className="navbar-left">
        <h2 className="logo">TenantFlow</h2>

        <nav className="nav-links">
          <Link to="/dashboard">Dashboard</Link>

          {(role === "user" || role === "tenant_admin") && (
            <Link to="/projects">Projects</Link>
          )}

          {role === "tenant_admin" && (
            <Link to="/users">Users</Link>
          )}

          {role === "super_admin" && (
            <Link to="/tenants">Tenants</Link>
          )}
        </nav>
      </div>

      <div className="navbar-right">
        <div className="user-info">
          <span className="name">{user.fullName}</span>
          <span className="role">{user.role}</span>
        </div>

        <button className="logout-btn" onClick={logout}>
          Logout
        </button>
      </div>
    </header>
  )
}
