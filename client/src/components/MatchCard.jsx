import { Link } from "react-router-dom";
import { format } from "date-fns";
import { Card } from "@/components/ui/card";
import StatusBadge from "./StatusBadge";

export default function MatchCard({ match }) {
    return (
        <Link to={`/matches/${match.id}`}>
            <Card className="p-4 hover:shadow-md transition-shadow cursor-pointer">
                <div className="flex items-center justify-between mb-3">
                    <StatusBadge status={match.status} />
                    <span className="text-xs text-muted-foreground">
                        {format(new Date(match.startTime), "dd MMM yyyy, HH:mm")}
                    </span>
                </div>

                <div className="flex items-center justify-between">
                    <div className="flex-1 text-right pr-3">
                        <div className="font-semibold">{match.homeTeam.name}</div>
                        <div className="text-xs text-muted-foreground">
                            {match.homeTeam.shortName}
                        </div>
                    </div>

                    <div className="px-4 text-2xl font-bold tabular-nums">
                        {match.status === "SCHEDULED" ? (
                            <span className="text-muted-foreground text-base font-normal">vs</span>
                        ) : (
                            `${match.homeScore} : ${match.awayScore}`
                        )}
                    </div>

                    <div className="flex-1 pl-3">
                        <div className="font-semibold">{match.awayTeam.name}</div>
                        <div className="text-xs text-muted-foreground">
                            {match.awayTeam.shortName}
                        </div>
                    </div>
                </div>
            </Card>
        </Link>
    );
}