import { Link } from "react-router-dom";
import { useDateFormat } from "@/lib/dateFormat";
import { Card } from "@/components/ui/card";
import StatusBadge from "./StatusBadge";
import {useTranslation} from "react-i18next";
import { useTeamName } from "@/lib/teamName";

export default function MatchCard({ match }) {
    const { t } = useTranslation();
    const format = useDateFormat();
    const getTeamName = useTeamName();

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
                        <div className="font-semibold">{getTeamName(match.homeTeam)}</div>
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
                        <div className="font-semibold">{getTeamName(match.awayTeam)}</div>
                        <div className="text-xs text-muted-foreground">
                            {match.awayTeam.shortName}
                        </div>
                    </div>
                </div>
            </Card>
        </Link>
    );
}