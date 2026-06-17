import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuth } from "@/contexts/AuthContext";
import { authService } from "@/services/authService";
import { useTranslation } from "react-i18next";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [submitting, setSubmitting] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const { t } = useTranslation();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            const data = await authService.login(username, password);
            login(data.token, data.username, data.role);
            toast.success(t("login.welcome", { name: data.username }))

            const redirectTo = location.state?.from?.pathname || "/admin";
            navigate(redirectTo, { replace: true });
        } catch (err) {
            const message = err.response?.data?.message || t("login.invalid_credentials");
            toast.error(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <div className="max-w-md mx-auto">
            <h1 className="text-3xl font-bold mb-6">{t("login.title")}</h1>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <Label htmlFor="username">{t("login.username")}</Label>
                    <Input
                        id="username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder={t("login.username_placeholder")}
                        required
                        autoFocus
                    />
                </div>
                <div>
                    <Label htmlFor="password">{t("login.password")}</Label>
                    <Input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <Button type="submit" className="w-full" disabled={submitting}>
                    {submitting ? t("login.submitting") : t("login.submit")}
                </Button>
            </form>
        </div>
    );
}