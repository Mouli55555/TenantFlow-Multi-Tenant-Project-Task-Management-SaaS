import "./Sidebar.css"

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <h2 className="logo">TenantFlow</h2>

      <nav className="nav">
        <a className="nav-item active">Dashboard</a>
        <a className="nav-item">Projects</a>
        <a className="nav-item">Users</a>
      </nav>
    </aside>
  )
}
