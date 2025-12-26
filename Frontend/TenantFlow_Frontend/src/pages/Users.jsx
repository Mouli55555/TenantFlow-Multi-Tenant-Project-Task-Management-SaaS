import { useEffect, useState } from "react"
import api from "../services/api"
import Navbar from "../components/Navbar"
import { useAuth } from "../context/AuthContext"
import "../styles/users.css"
import UserModal from "../components/UserModal"


export default function Users() {
    const { user } = useAuth()

    // 🔒 Role guard (PDF requirement)
    if (user.role !== "tenant_admin") {
        return (
            <>
                <Navbar />
                <div className="page-content">Access denied</div>
            </>
        )
    }

    const [showModal, setShowModal] = useState(false)
    const [editUser, setEditUser] = useState(null)
    const [users, setUsers] = useState([])
    const [search, setSearch] = useState("")
    const [role, setRole] = useState("")
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        loadUsers()
    }, [])

    const loadUsers = async () => {
        try {
            setLoading(true)
            const res = await api.get(`/tenants/${user.tenantId}/users`)
            setUsers(res.data.data.users || res.data.data)
        } catch {
            console.error("Failed to load users")
        } finally {
            setLoading(false)
        }
    }

    const deleteUser = async (userId) => {
        if (!window.confirm("Delete this user?")) return
        await api.delete(`/users/${userId}`)
        loadUsers()
    }

    const filteredUsers = users.filter(u =>
        u.fullName.toLowerCase().includes(search.toLowerCase()) ||
        u.email.toLowerCase().includes(search.toLowerCase())
    ).filter(u => role ? u.role === role : true)

    return (
        <>
            <Navbar />
            <div className="page-content">

                <div className="users-header">
                    <h1>Users</h1>
                    <button
                        className="btn btn-primary"
                        onClick={() => {
                            setEditUser(null)
                            setShowModal(true)
                        }}
                    >
                        Add User
                    </button>

                </div>

                <div className="filters">
                    <input
                        className="input"
                        placeholder="Search users..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />

                    <select
                        className="input"
                        value={role}
                        onChange={(e) => setRole(e.target.value)}
                    >
                        <option value="">All Roles</option>
                        <option value="user">User</option>
                        <option value="tenant_admin">Tenant Admin</option>
                    </select>
                </div>

                {loading ? (
                    <p>Loading users...</p>
                ) : filteredUsers.length === 0 ? (
                    <p className="muted">No users found</p>
                ) : (
                    <table className="users-table">
                        <thead>
                            <tr>
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Created</th>
                                <th>Actions</th>
                            </tr>
                        </thead>

                        <tbody>
                            {filteredUsers.map(u => (
                                <tr key={u.id}>
                                    <td>{u.fullName}</td>
                                    <td>{u.email}</td>
                                    <td>
                                        <span className={`badge ${u.role}`}>
                                            {u.role}
                                        </span>
                                    </td>
                                    <td>{u.isActive ? "Active" : "Inactive"}</td>
                                    <td>{new Date(u.createdAt).toLocaleDateString()}</td>
                                    <td>
                                        <button
                                            className="link-btn"
                                            onClick={() => {
                                                setEditUser(u)
                                                setShowModal(true)
                                            }}
                                        >
                                            Edit
                                        </button>

                                        <button
                                            className="link-btn danger"
                                            onClick={() => deleteUser(u.id)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
                <UserModal
                    isOpen={showModal}
                    user={editUser}
                    tenantId={user.tenantId}
                    onClose={() => setShowModal(false)}
                    onSuccess={loadUsers}
                />

            </div>
        </>
    )
}
