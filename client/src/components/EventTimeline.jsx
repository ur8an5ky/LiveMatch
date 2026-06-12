export default function EventTimeline({ events }) {
    if (events.length === 0) {
        return (
            <p className="text-muted-foreground text-center py-8">
                No events yet.
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
                        <span className="font-medium">{event.eventType}</span>
                        {event.teamName && (
                            <span className="text-sm text-muted-foreground">
                                - {event.teamName}
                            </span>
                        )}
                    </div>
                    {event.description && (
                        <p className="text-sm text-muted-foreground">
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