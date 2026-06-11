import { useState } from "react";
import { toast } from "sonner";
import { Plus } from "lucide-react";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card } from "@/components/ui/card";
import { matchService } from "@/services/matchService";

const EVENT_TYPES = [
    { value: "GOAL", label: "⚽ Goal" },
    { value: "OWN_GOAL", label: "⚽ Own goal" },
    { value: "YELLOW_CARD", label: "🟨 Yellow card" },
    { value: "RED_CARD", label: "🟥 Red card" },
    { value: "SUBSTITUTION", label: "🔄 Substitution" },
    { value: "PENALTY", label: "🎯 Penalty" },
];

export default function AddEventForm({ match }) {
    const [eventType, setEventType] = useState("GOAL");
    const [minute, setMinute] = useState("");
    const [teamId, setTeamId] = useState("");
    const [description, setDescription] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            await matchService.addEvent(match.id, {
                eventType,
                minuteOfMatch: Number(minute),
                teamId: Number(teamId),
                description: description || null,
            });

            setMinute("");
            setDescription("");
            toast.success("Event added");
        } catch (err) {
            const message = err.response?.data?.message || "Could not add event";
            toast.error(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Card className="p-4">
            <h3 className="font-semibold mb-4">Add event</h3>
            <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-5 gap-3">
                <div>
                    <Label htmlFor="eventType" className="text-xs">
                        Type
                    </Label>
                    <Select value={eventType} onValueChange={setEventType}>
                        <SelectTrigger id="eventType">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            {EVENT_TYPES.map((t) => (
                                <SelectItem key={t.value} value={t.value}>
                                    {t.label}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div>
                    <Label htmlFor="minute" className="text-xs">
                        Minute
                    </Label>
                    <Input
                        id="minute"
                        type="number"
                        min="0"
                        max="130"
                        value={minute}
                        onChange={(e) => setMinute(e.target.value)}
                        placeholder="45"
                        required
                    />
                </div>

                <div>
                    <Label htmlFor="team" className="text-xs">
                        Team
                    </Label>
                    <Select value={teamId} onValueChange={setTeamId}>
                        <SelectTrigger id="team">
                            <SelectValue placeholder="Select team" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value={String(match.homeTeam.id)}>
                                {match.homeTeam.shortName} (home)
                            </SelectItem>
                            <SelectItem value={String(match.awayTeam.id)}>
                                {match.awayTeam.shortName} (away)
                            </SelectItem>
                        </SelectContent>
                    </Select>
                </div>

                <div className="md:col-span-1">
                    <Label htmlFor="description" className="text-xs">
                        Description (optional)
                    </Label>
                    <Input
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Player name, notes..."
                    />
                </div>

                <div className="flex items-end">
                    <Button
                        type="submit"
                        className="w-full"
                        disabled={submitting || !minute || !teamId}
                    >
                        <Plus className="h-4 w-4 mr-1" />
                        {submitting ? "Adding..." : "Add"}
                    </Button>
                </div>
            </form>
        </Card>
    );
}