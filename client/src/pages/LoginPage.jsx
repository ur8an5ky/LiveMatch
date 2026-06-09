import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuth } from "@/contexts/AuthContext";
import { authService } from "@/services/authService";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [submitting, setSubmitting] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            const data = await authService.login(username, password);
            login(data.token, data.username, data.role);
            toast.success(`Welcome, ${data.username}`);

            const redirectTo = location.state?.from?.pathname || "/admin";
            navigate(redirectTo, { replace: true });
        } catch (err) {
            const message = err.response?.data?.message || "Invalid credentials";
            toast.error(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="max-w-md mx-auto">
            <h1 className="text-3xl font-bold mb-6">Admin login</h1>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <Label htmlFor="username">Username</Label>
                    <Input
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="admin"
                        required
                        autoFocus
                    />
                </div>
                <div>
                    <Label htmlFor="password">Password</Label>
                    <Input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <Button type="submit" className="w-full" disabled={submitting}>
                    {submitting ? "Signing in..." : "Sign in"}
                </Button>
            </form>
        </div>
    );
}