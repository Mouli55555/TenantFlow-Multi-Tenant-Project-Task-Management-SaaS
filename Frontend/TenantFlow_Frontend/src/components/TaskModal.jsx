import { useEffect, useState } from "react"
import api from "../services/api"

export default function TaskModal({
  isOpen,
  onClose,
  onSuccess,
  projectId,
  task = null,
  users = [],
}) {
  const isEdit = Boolean(task)

  const [form, setForm] = useState({
    title: "",
    description: "",
    status: "todo",
    priority: "medium",
    assignedTo: "",
    dueDate: "",
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")

  useEffect(() => {
    if (task) {
      setForm({
        title: task.title || "",
        description: task.description || "",
        status: task.status || "todo",
        priority: task.priority || "medium",
        assignedTo: task.assignedTo?.id || "",
        dueDate: task.dueDate || "",
      })
    }
  }, [task])

  if (!isOpen) return null

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    if (!form.title.trim()) {
      return setError("Task title is required")
    }

    try {
      setLoading(true)

      if (isEdit) {
        await api.put(`/tasks/${task.id}`, form)
      } else {
        await api.post(`/projects/${projectId}/tasks`, form)
      }

      onSuccess()
      onClose()
    } catch (err) {
      setError(err.response?.data?.message || "Task action failed")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="modal-overlay">
      <div className="modal">

        <h3>{isEdit ? "Edit Task" : "Add Task"}</h3>

        {error && <p className="error">{error}</p>}

        <form onSubmit={handleSubmit}>
          <input
            className="input"
            name="title"
            placeholder="Task Title"
            value={form.title}
            onChange={handleChange}
            required
          />

          <textarea
            className="input"
            name="description"
            placeholder="Description"
            rows={3}
            value={form.description}
            onChange={handleChange}
          />

          <select
            className="input"
            name="status"
            value={form.status}
            onChange={handleChange}
          >
            <option value="todo">Todo</option>
            <option value="in_progress">In Progress</option>
            <option value="completed">Completed</option>
          </select>

          <select
            className="input"
            name="priority"
            value={form.priority}
            onChange={handleChange}
          >
            <option value="low">Low</option>
            <option value="medium">Medium</option>
            <option value="high">High</option>
          </select>

          <select
            className="input"
            name="assignedTo"
            value={form.assignedTo}
            onChange={handleChange}
          >
            <option value="">Unassigned</option>
            {users.map(u => (
              <option key={u.id} value={u.id}>
                {u.fullName}
              </option>
            ))}
          </select>

          <input
            className="input"
            type="date"
            name="dueDate"
            value={form.dueDate}
            onChange={handleChange}
          />

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
