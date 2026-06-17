import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useDateFormat } from "@/lib/dateFormat";
import { Plus, Trash2, Settings } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Skeleton } from "@/components/ui/skeleton";
import StatusBadge from "@/components/StatusBadge";
import MatchFormDialog from "@/components/admin/MatchFormDialog";
import DeleteMatchDialog from "@/components/admin/DeleteMatchDialog";
import { matchService } from "@/services/matchService";

export default function MatchesAdminPage() {
    const [matches, setMatches] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showCreate, setShowCreate] = useState(false);
    const [deletingMatch, setDeletingMatch] = useState(null);
    const format = useDateFormat();

    useEffect(() => {
        matchService
            .getAll()
            .then(setMatches)
            .catch(() => toast.error("Could not load matches"))
            .finally(() => setLoading(false));
    }, []);

    const handleSaved = (saved) => {
        setMatches((prev) => [...prev, saved]);
    };

    const handleDeleted = (id) => {
        setMatches((prev) => prev.filter((m) => m.id !== id));
    };

    const statusOrder = { LIVE: 0, SCHEDULED: 1, FINISHED: 2, CANCELLED: 3 };
    const sorted = [...matches].sort(
        (a, b) => statusOrder[a.status] - statusOrder[b.status]
    );

    return (
        <div>
            <div className="mb-4">
                <Link
                    to="/admin"
                    className="text-sm text-muted-foreground hover:text-foreground"
                >
                    ← Back to admin
                </Link>
            </div>

            <div className="flex items-center justify-between mb-6">
                <h1 className="text-3xl font-bold">Matches</h1>
                <Button onClick={() => setShowCreate(true)}>
                    <Plus className="h-4 w-4 mr-2" />
                    New match
                </Button>
            </div>

            {loading ? (
                <div className="space-y-2">
                    {[1, 2, 3].map((i) => (
                        <Skeleton key={i} className="h-12 w-full" />
                    ))}
                </div>
            ) : matches.length === 0 ? (
                <p className="text-muted-foreground text-center py-12">
                    No matches yet. Click "New match" to schedule one.
                </p>
            ) : (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Status</TableHead>
                            <TableHead>Match</TableHead>
                            <TableHead>Score</TableHead>
                            <TableHead>Start time</TableHead>
                            <TableHead className="text-right">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {sorted.map((match) => (
                            <TableRow key={match.id}>
                                <TableCell>
                                    <StatusBadge status={match.status} />
                                </TableCell>
                                <TableCell className="font-medium">
                                    {match.homeTeam.name} vs {match.awayTeam.name}
                                </TableCell>
                                <TableCell className="tabular-nums">
                                    {match.status === "SCHEDULED"
                                        ? "-"
                                        : `${match.homeScore} : ${match.awayScore}`}
                                </TableCell>
                                <TableCell className="text-sm text-muted-foreground">
                                    {format(
                                        new Date(match.startTime),
                                        "dd MMM yyyy, HH:mm"
                                    )}
                                </TableCell>
                                <TableCell className="text-right">
                                    <Button variant="ghost" size="icon" asChild>
                                        <Link to={`/admin/matches/${match.id}/control`}>
                                            <Settings className="h-4 w-4" />
                                        </Link>
                                    </Button>
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        onClick={() => setDeletingMatch(match)}
                                        className="text-destructive hover:text-destructive"
                                    >
                                        <Trash2 className="h-4 w-4" />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}

            <MatchFormDialog
                open={showCreate}
                onOpenChange={setShowCreate}
                onSaved={handleSaved}
            />

            <DeleteMatchDialog
                open={deletingMatch !== null}
                onOpenChange={(open) => !open && setDeletingMatch(null)}
                match={deletingMatch}
                onDeleted={handleDeleted}
            />
        </div>
    );
}