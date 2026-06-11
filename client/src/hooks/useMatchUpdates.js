import { useEffect, useRef } from "react";
import { Client } from "@stomp/stompjs";

export function useMatchUpdates(matchId, { onEvent, onStatusChange }) {
    const seenEventIds = useRef(new Set());

    useEffect(() => {
        if (!matchId) return;

        const wsUrl =
            import.meta.env.VITE_API_URL.replace("http", "ws") + "/ws";

        const client = new Client({
            brokerURL: wsUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(
                    `/topic/matches/${matchId}/events`,
                    (message) => {
                        const newEvent = JSON.parse(message.body);
                        if (seenEventIds.current.has(newEvent.id)) return;
                        seenEventIds.current.add(newEvent.id);
                        onEvent?.(newEvent);
                    }
                );

                client.subscribe(
                    `/topic/matches/${matchId}/status`,
                    (message) => {
                        const change = JSON.parse(message.body);
                        onStatusChange?.(change);
                    }
                );
            },
        });

        client.activate();
        return () => client.deactivate();
    }, [matchId, onEvent, onStatusChange]);
}