import { useEffect, useState } from "react"
import api from "../services/api"

export default function UserModal({
  isOpen,
  onClose,
  onSuccess,
  tenantId,
  user = null,
}) {
  const isEdit = Boolean(user)

  const [form, setForm] = useState({
    email: "",
    fullName: "",
    password: "",
    role: "user",
    isActive: true,
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")

  useEffect(() => {
    if (user) {
      setForm({
        email: user.email,
        fullName: user.fullName,
        password: "",
        role: user.role,
        isActive: user.isActive,
      })
    }
  }, [user])

  if (!isOpen) return null

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target
    setForm({ ...form, [name]: type === "checkbox" ? checked : value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    if (!form.fullName.trim()) {
      return setError("Full name is required")
    }

    if (!isEdit && !form.password) {
      return setError("Password is required")
    }

    try {
      setLoading(true)

      if (isEdit) {
        await api.put(`/users/${user.id}`, {
          fullName: form.fullName,
          role: form.role,
          isActive: form.isActive,
        })
      } else {
        await api.post(`/tenants/${tenantId}/users`, {
          email: form.email,
          password: form.password,
          fullName: form.fullName,
          role: form.role,
        })
      }

      onSuccess()
      onClose()
    } catch (err) {
      setError(err.response?.data?.message || "Action failed")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal">
        <h3>{isEdit ? "Edit User" : "Add User"}</h3>

        {error && <p className="error">{error}</p>}

        <form onSubmit={handleSubmit}>
          {!isEdit && (
            <input
              className="input"
              name="email"
              placeholder="Email"
              type="email"
              required
              onChange={handleChange}
            />
          )}

          <input
            className="input"
            name="fullName"
            placeholder="Full Name"
            value={form.fullName}
            onChange={handleChange}
            required
          />

          {!isEdit && (
            <input
              className="input"
              name="password"
              placeholder="Password"
              type="password"
              onChange={handleChange}
              required
            />
          )}

          <select
            className="input"
            name="role"
            value={form.role}
            onChange={handleChange}
          >
            <option value="user">User</option>
            <option value="tenant_admin">Tenant Admin</option>
          </select>

          {isEdit && (
            <label className="checkbox">
              <input
                type="checkbox"
                name="isActive"
                checked={form.isActive}
                onChange={handleChange}
              />
              Active
            </label>
          )}

          <div className="modal-actions">
            <button
              type="button"
              className="btn"
              onClick={onClose}
              disabled={loading}
            >
              Cancel
            </button>

            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? "Saving..." : "Save"}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
