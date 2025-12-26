import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import api from "../services/api"
import Navbar from "../components/Navbar"
import "../styles/projectDetails.css"
import TaskModal from "../components/TaskModal"

export default function ProjectDetails() {
    
    const [showTaskModal, setShowTaskModal] = useState(false)
    const [editTask, setEditTask] = useState(null)
    const [users, setUsers] = useState([])
  const { projectId } = useParams()

  const [project, setProject] = useState(null)
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadProject()
  }, [])

  const loadUsers = async () => {
  const res = await api.get(`/tenants/${project.tenantId}/users`)
  setUsers(res.data.data.users || res.data.data)
}


  const loadProject = async () => {
    try {
      setLoading(true)
      const projectRes = await api.get(`/projects/${projectId}`)
      setProject(projectRes.data.data)

      const taskRes = await api.get(`/projects/${projectId}/tasks`)
      setTasks(taskRes.data.data.tasks || taskRes.data.data)
    } catch {
      console.error("Failed to load project")
    } finally {
      setLoading(false)
    }
  }

  const updateTaskStatus = async (taskId, status) => {
    await api.patch(`/tasks/${taskId}/status`, { status })
    loadProject()
  }

  const deleteTask = async (taskId) => {
    if (!window.confirm("Delete this task?")) return
    await api.delete(`/tasks/${taskId}`)
    loadProject()
  }

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="page-content">Loading project...</div>
      </>
    )
  }

  return (
    <>
      <Navbar />
      <div className="page-content project-details">

        {/* 🔹 PROJECT HEADER */}
        <div className="project-header">
          <div>
            <h1>{project.name}</h1>
            <p className="muted">{project.description}</p>
          </div>

          <span className={`badge ${project.status}`}>
            {project.status}
          </span>
        </div>

        {/* 🔹 TASKS SECTION */}
        <div className="tasks-section">
          <div className="tasks-header">
            <h2>Tasks</h2>
            <button
  className="btn btn-primary"
  onClick={() => {
    setEditTask(null)
    setShowTaskModal(true)
  }}
>
  Add Task
</button>

<button
  className="link-btn"
  onClick={() => {
    setEditTask(task)
    setShowTaskModal(true)
  }}
>
  Edit
</button>


          </div>

          {tasks.length === 0 ? (
            <p className="muted">No tasks found</p>
          ) : (
            <div className="tasks-list">
              {tasks.map(task => (
                <div key={task.id} className="task-card">
                  <div>
                    <strong>{task.title}</strong>
                    <p className="muted">
                      {task.priority} • {task.status}
                    </p>
                  </div>

                  <div className="task-actions">
                    <select
                      value={task.status}
                      onChange={(e) =>
                        updateTaskStatus(task.id, e.target.value)
                      }
                    >
                      <option value="todo">Todo</option>
                      <option value="in_progress">In Progress</option>
                      <option value="completed">Completed</option>
                    </select>

                    <button
                      className="link-btn danger"
                      onClick={() => deleteTask(task.id)}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}

        </div>
      </div>
      <TaskModal
  isOpen={showTaskModal}
  task={editTask}
  projectId={projectId}
  users={users}
  onClose={() => setShowTaskModal(false)}
  onSuccess={loadProject}
/>

    </>
  )
}
