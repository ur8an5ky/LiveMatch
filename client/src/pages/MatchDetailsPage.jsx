import { useParams } from "react-router-dom";

export default function MatchDetailsPage() {
    const { id } = useParams();
    return (
        <div>
            <h1 className="text-3xl font-bold mb-4">Match #{id}</h1>
            <p className="text-muted-foreground">
                Match details and live timeline will appear here.
            </p>
        </div>
    );
}