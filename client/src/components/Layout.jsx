import { Link, Outlet, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/contexts/AuthContext";
import { Sun, Moon } from "lucide-react";
import { useTheme } from "@/contexts/ThemeContext";
import {toast} from "sonner";

export default function Layout() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const { theme, toggle } = useTheme();

    const handleLogout = () => {
        logout();
        navigate("/");
        toast.success("Logged out");
    };

    return (
        <div className="min-h-screen bg-background">
            <header className="border-b">
                <div className="container mx-auto flex items-center justify-between py-4">
                    <Link to="/" className="text-xl font-bold text-primary">
                        ⚽ LiveMatch
                    </Link>
                    <nav className="flex items-center gap-4">
                        <Button variant="ghost" size="icon" onClick={toggle}>
                            {theme === "dark" ? (
                                <Sun className="h-4 w-4" />
                            ) : (
                                <Moon className="h-4 w-4" />
                            )}
                        </Button>
                        <Link to="/" className="text-sm hover:underline">
                            Matches
                        </Link>
                        {user ? (
                            <>
                                <Link to="/admin" className="text-sm hover:underline">
                                    Admin
                                </Link>
                                <span className="text-sm text-muted-foreground">
                                    {user.username}
                                </span>
                                <Button variant="outline" size="sm" onClick={handleLogout}>
                                    Logout
                                </Button>
                            </>
                        ) : (
                            <Link to="/login" className="text-sm hover:underline">
                                Login
                            </Link>
                        )}
                    </nav>
                </div>
            </header>

            <main className="container mx-auto py-8">
                <Outlet />
            </main>
        </div>
    );
}