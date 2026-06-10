import { useEffect, useState, useRef } from "react";
import { Client } from "@stomp/stompjs";
import { toast } from "sonner";
import { Skeleton } from "@/components/ui/skeleton";
import MatchCard from "@/components/MatchCard";
import { matchService } from "@/services/matchService";

export default function MatchListPage() {
    const [matches, setMatches] = useState([]);
    const [loading, setLoading] = useState(true);
    const matchesRef = useRef([]);
    const stompClientRef = useRef(null);

    useEffect(() => {
        matchesRef.current = matches;
    }, [matches]);

    useEffect(() => {
        matchService
            .getAll()
            .then(setMatches)
            .catch(() => toast.error("Could not load matches"))
            .finally(() => setLoading(false));
    }, []);

    useEffect(() => {
        if (loading || matches.length === 0) return;

        const wsUrl = import.meta.env.VITE_API_URL.replace("http", "ws") + "/ws";
        const client = new Client({
            brokerURL: wsUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                matches.forEach((match) => {
                    client.subscribe(
                        `/topic/matches/${match.id}/events`,
                        (message) => {
                            const newEvent = JSON.parse(message.body);
                            const current = matchesRef.current.find(
                                (m) => m.id === match.id
                            );
                            if (!current) return;

                            const isGoal = newEvent.eventType === "GOAL";
                            const isHome = newEvent.teamId === current.homeTeam.id;
                            const newHomeScore =
                                isGoal && isHome
                                    ? current.homeScore + 1
                                    : current.homeScore;
                            const newAwayScore =
                                isGoal && !isHome
                                    ? current.awayScore + 1
                                    : current.awayScore;

                            setMatches((prev) =>
                                prev.map((m) =>
                                    m.id === match.id
                                        ? {
                                            ...m,
                                            homeScore: newHomeScore,
                                            awayScore: newAwayScore,
                                        }
                                        : m
                                )
                            );

                            toast.info(
                                `${newEvent.eventType} by ${newEvent.teamName}!`,
                                {
                                    duration: 6000,
                                    description: (
                                        <div className="space-y-1">
                                            <div>
                                                {newEvent.minuteOfMatch}'{" "}
                                                {newEvent.description}
                                            </div>
                                            <div className="font-semibold">
                                                {current.homeTeam.shortName}{" "}
                                                {newHomeScore} : {newAwayScore}{" "}
                                                {current.awayTeam.shortName}
                                            </div>
                                        </div>
                                    ),
                                }
                            );
                        }
                    );

                    client.subscribe(
                        `/topic/matches/${match.id}/status`,
                        (message) => {
                        const change = JSON.parse(message.body);
                        setMatches((prev) =>
                            prev.map((m) =>
                                m.id === match.id ? { ...m, status: change.newStatus } : m
                            )
                        );
                    });

                    client.subscribe("/topic/matches/created", (message) => {
                        const newMatch = JSON.parse(message.body);
                        setMatches((prev) => {
                            if (prev.some((m) => m.id === newMatch.id)) return prev;
                            return [...prev, newMatch];
                        });
                        toast.info(
                            `New match: ${newMatch.homeTeam.shortName} vs ${newMatch.awayTeam.shortName}`
                        );
                    });
                });
            },
        });

        client.activate();
        stompClientRef.current = client;

        return () => client.deactivate();
    }, [loading]);

    const grouped = {
        LIVE: matches.filter((m) => m.status === "LIVE"),
        SCHEDULED: matches.filter((m) => m.status === "SCHEDULED"),
        FINISHED: matches.filter((m) => m.status === "FINISHED"),
        CANCELLED: matches.filter((m) => m.status === "CANCELLED"),
    };

    if (loading) {
        return (
            <div className="space-y-4">
                <h1 className="text-3xl font-bold mb-6">Matches</h1>
                {[1, 2, 3].map((i) => (
                    <Skeleton key={i} className="h-28 w-full" />
                ))}
            </div>
        );
    }

    if (matches.length === 0) {
        return (
            <div>
                <h1 className="text-3xl font-bold mb-6">Matches</h1>
                <p className="text-muted-foreground">No matches yet.</p>
            </div>
        );
    }

    return (
        <div>
            <h1 className="text-3xl font-bold mb-6">Matches</h1>

            {Object.entries(grouped).map(([status, list]) =>
                list.length > 0 ? (
                    <section key={status} className="mb-8">
                        <h2 className="text-xl font-semibold mb-3 text-muted-foreground">
                            {status} ({list.length})
                        </h2>
                        <div className="space-y-3">
                            {list.map((match) => (
                                <MatchCard key={match.id} match={match} />
                            ))}
                        </div>
                    </section>
                ) : null
            )}
        </div>
    );
}