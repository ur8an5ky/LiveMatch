import { Link } from "react-router-dom";

export default function NotFoundPage() {
    return (
        <div className="text-center py-12">
            <h1 className="text-4xl font-bold mb-4">404</h1>
            <p className="text-muted-foreground mb-4">Page not found.</p>
            <Link to="/" className="text-primary hover:underline">
                Go home
            </Link>
        </div>
    );
}