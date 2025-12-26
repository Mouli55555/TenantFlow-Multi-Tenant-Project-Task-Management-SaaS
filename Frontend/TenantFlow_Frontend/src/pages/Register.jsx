import { useState } from "react"
import { useNavigate } from "react-router-dom"
import api from "../services/api"
import "../styles/register.css"

export default function Register() {
  const navigate = useNavigate()

  const [form, setForm] = useState({
    tenantName: "",
    subdomain: "",
    adminEmail: "",
    adminFullName: "",
    password: "",
    confirmPassword: "",
    terms: false,
  })

  const [loading, setLoading] = useState(false)
  const [error, setError] = useState("")
  const [success, setSuccess] = useState("")

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target
    setForm({ ...form, [name]: type === "checkbox" ? checked : value })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError("")
    setSuccess("")

    // ✅ Client-side validation (PDF requirement)
    if (form.password !== form.confirmPassword) {
      return setError("Passwords do not match")
    }

    if (!form.terms) {
      return setError("Please accept Terms & Conditions")
    }

    try {
      setLoading(true)

      await api.post("/auth/register-tenant", {
        tenantName: form.tenantName,
        subdomain: form.subdomain,
        adminEmail: form.adminEmail,
        adminPassword: form.password,
        adminFullName: form.adminFullName,
      })

      setSuccess("Tenant registered successfully. Redirecting to login...")
      setTimeout(() => navigate("/login"), 2000)
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed")
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h2>Create your organization</h2>

        {error && <p className="error">{error}</p>}
        {success && <p className="success">{success}</p>}

        <input
          className="input"
          name="tenantName"
          placeholder="Organization Name"
          required
          onChange={handleChange}
        />

        <div className="subdomain">
          <input
            className="input"
            name="subdomain"
            placeholder="Subdomain"
            required
            onChange={handleChange}
          />
          <span>.yourapp.com</span>
        </div>

        <input
          className="input"
          name="adminEmail"
          type="email"
          placeholder="Admin Email"
          required
          onChange={handleChange}
        />

        <input
          className="input"
          name="adminFullName"
          placeholder="Admin Full Name"
          required
          onChange={handleChange}
        />

        <input
          className="input"
          name="password"
          type="password"
          placeholder="Password"
          required
          onChange={handleChange}
        />

        <input
          className="input"
          name="confirmPassword"
          type="password"
          placeholder="Confirm Password"
          required
          onChange={handleChange}
        />

        <label className="checkbox">
          <input
            type="checkbox"
            name="terms"
            onChange={handleChange}
          />
          I agree to Terms & Conditions
        </label>

        <button className="btn btn-primary" disabled={loading}>
          {loading ? "Creating..." : "Register"}
        </button>

        <p className="link">
          Already have an account? <a href="/login">Login</a>
        </p>
      </form>
    </div>
  )
}
