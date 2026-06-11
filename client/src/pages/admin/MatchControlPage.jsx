import { useEffect, useState, useCallback } from "react";
import { useParams, Link } from "react-router-dom";
import { format } from "date-fns";
import { toast } from "sonner";
import { Play, Square, Ban, ExternalLink } from "lucide-react";
import { Button } from "@/components/ui/button.jsx";
import { Card } from "@/components/ui/card.jsx";
import { Skeleton } from "@/components/ui/skeleton.jsx";
import StatusBadge from "@/components/StatusBadge.jsx";
import EventTimeline from "@/components/EventTimeline.jsx";
import AddEventForm from "@/components/admin/AddEventForm.jsx";
import { matchService } from "@/services/matchService.js";
import { useMatchUpdates } from "@/hooks/useMatchUpdates.js";

export default function MatchControlPage() {
    const { id } = useParams();
    const [match, setMatch] = useState(null);
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [statusChanging, setStatusChanging] = useState(false);

    // Initial load
    useEffect(() => {
        Promise.all([matchService.getById(id), matchService.getEvents(id)])
            .then(([m, e]) => {
                setMatch(m);
                setEvents(e);
            })
            .catch(() => toast.error("Could not load match"))
            .finally(() => setLoading(false));
    }, [id]);

    // Live updates via WebSocket
    const onEvent = useCallback((newEvent) => {
        setEvents((prev) => [...prev, newEvent]);
        if (newEvent.eventType === "GOAL") {
            setMatch((prev) => {
                if (!prev) return prev;
                const isHome = newEvent.teamId === prev.homeTeam.id;
                return {
                    ...prev,
                    homeScore: isHome ? prev.homeScore + 1 : prev.homeScore,
                    awayScore: !isHome ? prev.awayScore + 1 : prev.awayScore,
                };
            });
        }
    }, []);

    const onStatusChange = useCallback((change) => {
        setMatch((prev) => (prev ? { ...prev, status: change.newStatus } : prev));
    }, []);

    useMatchUpdates(id, { onEvent, onStatusChange });

    const handleChangeStatus = async (newStatus) => {
        setStatusChanging(true);
        try {
            await matchService.updateStatus(id, newStatus);
            toast.success(`Match status changed to ${newStatus}`);
            // setMatch will happen via WebSocket onStatusChange
        } catch (err) {
            const message = err.response?.data?.message || "Could not change status";
            toast.error(message);
        } finally {
            setStatusChanging(false);
        }
    };

    if (loading) {
        return (
            <div className="space-y-4">
                <Skeleton className="h-12 w-1/3" />
                <Skeleton className="h-32 w-full" />
                <Skeleton className="h-32 w-full" />
                <Skeleton className="h-64 w-full" />
            </div>
        );
    }

    if (!match) {
        return (
            <div className="text-center py-12">
                <p className="text-muted-foreground mb-4">Match not found.</p>
                <Link to="/admin/matches" className="text-primary hover:underline">
                    ← Back to matches
                </Link>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto">
            <div className="flex items-center justify-between mb-4">
                <Link
                    to="/admin/matches"
                    className="text-sm text-muted-foreground hover:text-foreground"
                >
                    ← Back to matches
                </Link>
                <Button variant="outline" size="sm" asChild>
                    <Link to={`/matches/${id}`} target="_blank" rel="noreferrer">
                        <ExternalLink className="h-3 w-3 mr-1" />
                        View as fan
                    </Link>
                </Button>
            </div>

            <Card className="p-6 mb-4">
                <div className="flex items-center justify-between mb-4">
                    <StatusBadge status={match.status} />
                    <span className="text-sm text-muted-foreground">
                        {format(new Date(match.startTime), "dd MMM yyyy, HH:mm")}
                    </span>
                </div>

                <div className="flex items-center justify-between">
                    <div className="flex-1 text-right pr-4">
                        <div className="text-xl font-bold">{match.homeTeam.name}</div>
                    </div>

                    <div className="px-6 text-4xl font-bold tabular-nums">
                        {match.status === "SCHEDULED" ? (
                            <span className="text-muted-foreground text-2xl font-normal">
                                vs
                            </span>
                        ) : (
                            `${match.homeScore} : ${match.awayScore}`
                        )}
                    </div>

                    <div className="flex-1 pl-4">
                        <div className="text-xl font-bold">{match.awayTeam.name}</div>
                    </div>
                </div>
            </Card>

            <Card className="p-4 mb-4">
                <h3 className="font-semibold mb-3">Match control</h3>
                <div className="flex flex-wrap gap-2">
                    {match.status === "SCHEDULED" && (
                        <>
                            <Button
                                onClick={() => handleChangeStatus("LIVE")}
                                disabled={statusChanging}
                            >
                                <Play className="h-4 w-4 mr-1" />
                                Start match
                            </Button>
                            <Button
                                variant="outline"
                                onClick={() => handleChangeStatus("CANCELLED")}
                                disabled={statusChanging}
                            >
                                <Ban className="h-4 w-4 mr-1" />
                                Cancel match
                            </Button>
                        </>
                    )}
                    {match.status === "LIVE" && (
                        <Button
                            variant="destructive"
                            onClick={() => handleChangeStatus("FINISHED")}
                            disabled={statusChanging}
                        >
                            <Square className="h-4 w-4 mr-1" />
                            End match
                        </Button>
                    )}
                    {(match.status === "FINISHED" || match.status === "CANCELLED") && (
                        <p className="text-sm text-muted-foreground">
                            This match has ended. No further actions available.
                        </p>
                    )}
                </div>
            </Card>

            {match.status === "LIVE" && (
                <div className="mb-4">
                    <AddEventForm match={match} />
                </div>
            )}

            <h2 className="text-xl font-semibold mb-4">Timeline</h2>
            <EventTimeline events={events} />
        </div>
    );
}