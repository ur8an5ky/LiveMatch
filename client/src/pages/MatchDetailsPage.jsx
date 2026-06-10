import { useEffect, useState, useRef } from "react";
import { useParams, Link } from "react-router-dom";
import { Client } from "@stomp/stompjs";
import { format } from "date-fns";
import { toast } from "sonner";
import { Skeleton } from "@/components/ui/skeleton";
import { Card } from "@/components/ui/card";
import StatusBadge from "@/components/StatusBadge";
import EventTimeline from "@/components/EventTimeline";
import { matchService } from "@/services/matchService";

export default function MatchDetailsPage() {
    const { id } = useParams();
    const [match, setMatch] = useState(null);
    const [events, setEvents] = useState([]);
    const [loading, setLoading] = useState(true);
    const stompClientRef = useRef(null);
    const matchRef = useRef(null);

    useEffect(() => {
        matchRef.current = match;
    }, [match]);

    useEffect(() => {
        Promise.all([matchService.getById(id), matchService.getEvents(id)])
            .then(([matchData, eventsData]) => {
                setMatch(matchData);
                setEvents(eventsData);
            })
            .catch(() => toast.error("Could not load match"))
            .finally(() => setLoading(false));
    }, [id]);

    useEffect(() => {
        const wsUrl = import.meta.env.VITE_API_URL.replace("http", "ws") + "/ws";

        const client = new Client({
            brokerURL: wsUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/topic/matches/${id}/events`, (message) => {
                    const newEvent = JSON.parse(message.body);
                    setEvents((prev) => [...prev, newEvent]);

                    const current = matchRef.current;
                    if (!current) return;

                    const isGoal = newEvent.eventType === "GOAL";
                    const isHome = newEvent.teamId === current.homeTeam.id;
                    const newHomeScore = isGoal && isHome ? current.homeScore + 1 : current.homeScore;
                    const newAwayScore = isGoal && !isHome ? current.awayScore + 1 : current.awayScore;

                    setMatch({ ...current, homeScore: newHomeScore, awayScore: newAwayScore });

                    toast.info(`${newEvent.eventType} by ${newEvent.teamName}!`, {
                        duration: 6000,
                        description: (
                            <div className="space-y-1">
                                <div>
                                    {newEvent.minuteOfMatch}' {newEvent.description}
                                </div>
                                <div className="font-semibold">
                                    {current.homeTeam.shortName} {newHomeScore} : {newAwayScore}{" "}
                                    {current.awayTeam.shortName}
                                </div>
                            </div>
                        ),
                    });
                });

                client.subscribe(`/topic/matches/${id}/status`, (message) => {
                    const change = JSON.parse(message.body);
                    setMatch((prev) =>
                        prev ? { ...prev, status: change.newStatus } : prev
                    );
                });
            },
            onStompError: (frame) => {
                console.error("STOMP error", frame);
            },
        });

        client.activate();
        stompClientRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [id]);

    if (loading) {
        return (
            <div className="space-y-4">
                <Skeleton className="h-12 w-1/3" />
                <Skeleton className="h-32 w-full" />
                <Skeleton className="h-64 w-full" />
            </div>
        );
    }

    if (!match) {
        return (
            <div className="text-center py-12">
                <p className="text-muted-foreground mb-4">Match not found.</p>
                <Link to="/" className="text-primary hover:underline">
                    ← Back to matches
                </Link>
            </div>
        );
    }

    return (
        <div className="max-w-3xl mx-auto">
            <Link
                to="/"
                className="text-sm text-muted-foreground hover:text-foreground mb-4 inline-block"
            >
                ← Back to matches
            </Link>

            <Card className="p-6 mb-6">
                <div className="flex items-center justify-between mb-4">
                    <StatusBadge status={match.status} />
                    <span className="text-sm text-muted-foreground">
                        {format(new Date(match.startTime), "dd MMM yyyy, HH:mm")}
                    </span>
                </div>

                <div className="flex items-center justify-between">
                    <div className="flex-1 text-right pr-4">
                        <div className="text-2xl font-bold">{match.homeTeam.name}</div>
                        <div className="text-sm text-muted-foreground">
                            {match.homeTeam.country}
                        </div>
                    </div>

                    <div className="px-6 text-5xl font-bold tabular-nums">
                        {match.status === "SCHEDULED" ? (
                            <span className="text-muted-foreground text-3xl font-normal">vs</span>
                        ) : (
                            `${match.homeScore} : ${match.awayScore}`
                        )}
                    </div>

                    <div className="flex-1 pl-4">
                        <div className="text-2xl font-bold">{match.awayTeam.name}</div>
                        <div className="text-sm text-muted-foreground">
                            {match.awayTeam.country}
                        </div>
                    </div>
                </div>
            </Card>

            <h2 className="text-xl font-semibold mb-4">Timeline</h2>
            <EventTimeline events={events} />
        </div>
    );
}