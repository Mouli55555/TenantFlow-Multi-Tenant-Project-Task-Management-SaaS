import { useState } from "react"
import { useNavigate, Link } from "react-router-dom"
import api from "../services/api"
import "../styles/login.css"

export default function Login() {
  const navigate = useNavigate()

  const [form, setForm] = useState({
    email: "",
    password: "",
    tenantSubdomain: "",
    remember: false,
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target
    setForm({ ...form, [name]: type === "checkbox" ? checked : value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")

    try {
      setLoading(true)

      const res = await api.post("/auth/login", {
        email: form.email,
        password: form.password,
        tenantSubdomain: form.tenantSubdomain,
      })

      const { token, user } = res.data.data

      // ✅ Store JWT (PDF requirement)
      if (form.remember) {
        localStorage.setItem("token", token)
      } else {
        sessionStorage.setItem("token", token)
      }

      localStorage.setItem("user", JSON.stringify(user))

      navigate("/dashboard")
    } catch (err) {
      setError(err.response?.data?.message || "Invalid login credentials")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h2>Sign in</h2>

        {error && <p className="error">{error}</p>}

        <input
          className="input"
          type="email"
          name="email"
          placeholder="Email"
          required
          onChange={handleChange}
        />

        <input
          className="input"
          type="password"
          name="password"
          placeholder="Password"
          required
          onChange={handleChange}
        />

        <input
          className="input"
          name="tenantSubdomain"
          placeholder="Tenant Subdomain"
          required
          onChange={handleChange}
        />

        <label className="checkbox">
          <input
            type="checkbox"
            name="remember"
            onChange={handleChange}
          />
          Remember me
        </label>

        <button className="btn btn-primary" disabled={loading}>
          {loading ? "Signing in..." : "Login"}
        </button>

        <p className="link">
          Don’t have an organization? <Link to="/register">Register</Link>
        </p>
      </form>
    </div>
  )
}
