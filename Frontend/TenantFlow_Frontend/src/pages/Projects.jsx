import { useEffect, useState } from "react"
import api from "../services/api"
import Navbar from "../components/Navbar"
import "../styles/projects.css"
import ProjectModal from "../components/ProjectModal"

export default function Projects() {

    const [showModal, setShowModal] = useState(false)
    const [editProject, setEditProject] = useState(null)
    const [projects, setProjects] = useState([])
    const [search, setSearch] = useState("")
    const [status, setStatus] = useState("")
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        loadProjects()
    }, [])

    const loadProjects = async () => {
        try {
            setLoading(true)
            const res = await api.get("/projects")
            setProjects(res.data.data.projects || res.data.data)
        } catch {
            console.error("Failed to load projects")
        } finally {
            setLoading(false)
        }
    }

    const deleteProject = async (id) => {
        if (!window.confirm("Delete this project?")) return
        await api.delete(`/projects/${id}`)
        loadProjects()
    }

    const filteredProjects = projects.filter(p =>
        p.name.toLowerCase().includes(search.toLowerCase()) &&
        (status ? p.status === status : true)
    )

    return (
        <>
            <Navbar />
            <div className="page-content">

                <div className="projects-header">
                    <h1>Projects</h1>
                    <button className="btn btn-primary">Create Project</button>
                </div>

                <div className="filters">
                    <input
                        className="input"
                        placeholder="Search projects..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />

                    <select
                        className="input"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                    >
                        <option value="">All Status</option>
                        <option value="active">Active</option>
                        <option value="archived">Archived</option>
                        <option value="completed">Completed</option>
                    </select>
                </div>

                {loading ? (
                    <p>Loading projects...</p>
                ) : filteredProjects.length === 0 ? (
                    <p className="muted">No projects found</p>
                ) : (
                    <div className="projects-grid">
                        {filteredProjects.map(project => (
                            <div key={project.id} className="project-card">
                                <h3>{project.name}</h3>
                                <p className="muted">{project.description || "No description"}</p>

                                <div className="meta">
                                    <span className={`badge ${project.status}`}>
                                        {project.status}
                                    </span>
                                    <span>{project.taskCount || 0} tasks</span>
                                </div>

                                <div className="actions">
                                    <button
                                        className="btn btn-primary"
                                        onClick={() => {
                                            setEditProject(null)
                                            setShowModal(true)
                                        }}
                                    >
                                        Create Project
                                    </button>
                                    <button
                                        className="link-btn"
                                        onClick={() => {
                                            setEditProject(project)
                                            setShowModal(true)
                                        }}
                                    >
                                        Edit
                                    </button>
                                    <ProjectModal
                                        isOpen={showModal}
                                        project={editProject}
                                        onClose={() => setShowModal(false)}
                                        onSuccess={loadProjects}
                                    />

                                    <button
                                        className="link-btn danger"
                                        onClick={() => deleteProject(project.id)}
                                    >
                                        Delete
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

            </div>
        </>
    )
}
