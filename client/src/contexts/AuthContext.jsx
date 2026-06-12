import { createContext, useContext, useEffect, useState } from "react";
import { toast } from "sonner";

const AuthContext = createContext(null);

function parseJwt(token) {
    try {
        const base64 = token.split('.')[1];
        const json = atob(base64.replace(/-/g, '+').replace(/_/g, '/'));
        return JSON.parse(json);
    } catch {
        return null;
    }
}

function getExpiresInMs(token) {
    if (!token) return 0;
    const payload = parseJwt(token);
    if (!payload?.exp) return 0;
    return payload.exp * 1000 - Date.now();
}

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        const token = localStorage.getItem("token");

        if (storedUser && token) {
            if (getExpiresInMs(token) > 0) {
                try {
                    setUser(JSON.parse(storedUser));
                } catch {
                    localStorage.removeItem("user");
                }
            } else {
                localStorage.removeItem("token");
                localStorage.removeItem("user");
            }
        }
        setLoading(false);
    }, []);

    useEffect(() => {
        if (!user) return;

        const token = localStorage.getItem("token");
        const expiresInMs = getExpiresInMs(token);
        if (expiresInMs <= 0) return;

        const timer = setTimeout(() => {
            logout();
            // window.location.href = "/login";
            toast.warning("Your session has expired. Please log in again.", {
                duration: 6000,
            });
        }, expiresInMs);

        return () => clearTimeout(timer);
    }, [user]);

    const login = (token, username, role) => {
        localStorage.setItem("token", token);
        const userData = { username, role };
        localStorage.setItem("user", JSON.stringify(userData));
        setUser(userData);
    };

    const logout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        setUser(null);
    };

    const isAdmin = user?.role === "ROLE_ADMIN";

    return (
        <AuthContext.Provider value={{ user, isAdmin, loading, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) {
        throw new Error("useAuth must be used within AuthProvider");
    }
    return ctx;
}