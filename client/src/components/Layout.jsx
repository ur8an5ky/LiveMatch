import { Link, Outlet } from "react-router-dom";

export default function Layout() {
    return (
        <div className="min-h-screen bg-background">
            <header className="border-b">
                <div className="container mx-auto flex items-center justify-between py-4">
                    <Link to="/" className="text-xl font-bold text-primary">
                        ⚽ LiveMatch
                    </Link>
                    <nav className="flex gap-4">
                        <Link to="/" className="text-sm hover:underline">
                            Matches
                        </Link>
                        <Link to="/login" className="text-sm hover:underline">
                            Admin
                        </Link>
                    </nav>
                </div>
            </header>

            <main className="container mx-auto py-8">
                <Outlet />
            </main>
        </div>
    );
}