import { useEffect, useState } from "react"
import api from "../services/api"
import "../styles/modal.css"

export default function ProjectModal({
  isOpen,
  onClose,
  onSuccess,
  project = null,
}) {
  const isEdit = Boolean(project)

  const [form, setForm] = useState({
    name: "",
    description: "",
    status: "active",
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")

  useEffect(() => {
    if (project) {
      setForm({
        name: project.name || "",
        description: project.description || "",
        status: project.status || "active",
      })
    }
  }, [project])

  if (!isOpen) return null

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    if (!form.name.trim()) {
      return setError("Project name is required")
    }

    try {
      setLoading(true)

      if (isEdit) {
        await api.put(`/projects/${project.id}`, form)
      } else {
        await api.post("/projects", form)
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

        <h3>{isEdit ? "Edit Project" : "Create Project"}</h3>

        {error && <p className="error">{error}</p>}

        <form onSubmit={handleSubmit}>
          <input
            className="input"
            name="name"
            placeholder="Project Name"
            value={form.name}
            onChange={handleChange}
            required
          />

          <textarea
            className="input"
            name="description"
            placeholder="Description"
            value={form.description}
            onChange={handleChange}
            rows={3}
          />

          <select
            className="input"
            name="status"
            value={form.status}
            onChange={handleChange}
          >
            <option value="active">Active</option>
            <option value="archived">Archived</option>
            <option value="completed">Completed</option>
          </select>

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
