import {useTranslation} from "react-i18next";
import { useTeamName } from "@/lib/teamName";

export default function EventTimeline({ events, match }) {
    const { t } = useTranslation();
    const getTeamName = useTeamName();

    const getEventTeamName = (event) => {
        if (!event.teamId || !match) return getEventTeamName(event) || "";
        if (event.teamId === match.homeTeam?.id) return getTeamName(match.homeTeam);
        if (event.teamId === match.awayTeam?.id) return getTeamName(match.awayTeam);
        return getEventTeamName(event) || "";
    };

    if (events.length === 0) {
        return (
            <p className="text-muted-foreground text-center py-8">
                {t("timeline.empty")}
            </p>
        );
    }

    return (
        <ol className="relative border-l border-border ml-3">
            {events.map((event) => (
                <li key={event.id} className="mb-6 ml-6">
                    <div className="absolute -left-1.5 mt-1.5 h-3 w-3 rounded-full bg-primary" />
                    <div className="flex items-center gap-2 mb-1">
                        <span className="font-mono text-sm font-semibold text-primary">
                            {event.minuteOfMatch}'
                        </span>
                        <EventTypeIcon type={event.eventType} />
                        <span className="font-medium">{t(`event_type.${event.eventType}`)}</span>
                        {getEventTeamName(event) && (
                            <span className="text-sm text-muted-foreground">
                                - {getEventTeamName(event)}
                            </span>
                        )}
                    </div>
                    {event.description && (
                        <p className="text-base text-muted-foreground">
                            {event.description}
                        </p>
                    )}
                </li>
            ))}
        </ol>
    );
}

function EventTypeIcon({ type }) {
    const icons = {
        GOAL: "⚽",
        YELLOW_CARD: "🟨",
        RED_CARD: "🟥",
        SUBSTITUTION: "🔄",
        PENALTY: "🎯",
        OWN_GOAL: "⚽",
    };
    return <span>{icons[type] || "📝"}</span>;
}