import { useEffect, useState } from "react"
import api from "../services/api"
import Navbar from "../components/Navbar"
import "../styles/dashboard.css"

export default function Dashboard() {
  const [stats, setStats] = useState({
    projects: 0,
    tasks: 0,
    completed: 0,
    pending: 0,
  })

  const [projects, setProjects] = useState([])
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    loadDashboard()
  }, [])

  const loadDashboard = async () => {
    try {
      setLoading(true)

      // 1️⃣ Get projects
      const projectsRes = await api.get("/projects")
      const projectList = projectsRes.data.data.projects || projectsRes.data.data

      setProjects(projectList.slice(0, 5))

      let totalTasks = 0
      let completedTasks = 0
      let myTasks = []

      // 2️⃣ Get tasks per project
      for (const project of projectList) {
        const tasksRes = await api.get(`/projects/${project.id}/tasks`)
        const taskList = tasksRes.data.data.tasks || tasksRes.data.data

        totalTasks += taskList.length
        completedTasks += taskList.filter(t => t.status === "completed").length
        myTasks = myTasks.concat(taskList)
      }

      setTasks(myTasks.slice(0, 5))

      setStats({
        projects: projectList.length,
        tasks: totalTasks,
        completed: completedTasks,
        pending: totalTasks - completedTasks,
      })
    } catch (err) {
      console.error("Dashboard load failed")
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="page-content">Loading dashboard...</div>
      </>
    )
  }

  return (
    <>
      <Navbar />
      <div className="dashboard page-content">

        {/* 🔢 STATISTICS */}
        <div className="stats-grid">
          <StatCard title="Total Projects" value={stats.projects} />
          <StatCard title="Total Tasks" value={stats.tasks} />
          <StatCard title="Completed Tasks" value={stats.completed} />
          <StatCard title="Pending Tasks" value={stats.pending} />
        </div>

        {/* 📁 RECENT PROJECTS */}
        <section>
          <h3>Recent Projects</h3>

          {projects.length === 0 ? (
            <p className="muted">No projects found</p>
          ) : (
            <div className="list">
              {projects.map(project => (
                <div key={project.id} className="list-item">
                  <div>
                    <strong>{project.name}</strong>
                    <p className="muted">{project.status}</p>
                  </div>
                  <span>{project.taskCount || 0} tasks</span>
                </div>
              ))}
            </div>
          )}
        </section>

        {/* 📝 MY TASKS */}
        <section>
          <h3>My Tasks</h3>

          {tasks.length === 0 ? (
            <p className="muted">No tasks assigned</p>
          ) : (
            <div className="list">
              {tasks.map(task => (
                <div key={task.id} className="list-item">
                  <div>
                    <strong>{task.title}</strong>
                    <p className="muted">
                      {task.priority} • {task.status}
                    </p>
                  </div>
                  <span>{task.dueDate || "-"}</span>
                </div>
              ))}
            </div>
          )}
        </section>

      </div>
    </>
  )
}

function StatCard({ title, value }) {
  return (
    <div className="stat-card">
      <p className="muted">{title}</p>
      <h2>{value}</h2>
    </div>
  )
}
