import { createContext, useContext, useEffect, useState } from "react"
import api from "../services/api"
import { useNavigate } from "react-router-dom"

const AuthContext = createContext()

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  // 🔐 Verify token on app load (PDF requirement)
  useEffect(() => {
    const token =
      localStorage.getItem("token") || sessionStorage.getItem("token")

    if (!token) {
      setLoading(false)
      return
    }

    api
      .get("/auth/me")
      .then((res) => {
        setUser(res.data.data)
      })
      .catch(() => {
        logout()
      })
      .finally(() => setLoading(false))
  }, [])

  const logout = () => {
    localStorage.clear()
    sessionStorage.clear()
    setUser(null)
    navigate("/login")
  }

  return (
    <AuthContext.Provider value={{ user, loading, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
