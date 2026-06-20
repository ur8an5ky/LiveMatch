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
import {useTranslation} from "react-i18next";

const EVENT_TYPES = [
    { value: "GOAL", icon: "⚽" },
    { value: "OWN_GOAL", icon: "⚽" },
    { value: "YELLOW_CARD", icon: "🟨" },
    { value: "RED_CARD", icon: "🟥" },
    { value: "SUBSTITUTION", icon: "🔄" },
    { value: "PENALTY", icon: "🎯" },
];

export default function AddEventForm({ match }) {
    const [eventType, setEventType] = useState("GOAL");
    const [minute, setMinute] = useState("");
    const [teamId, setTeamId] = useState("");
    const [description, setDescription] = useState("");
    const [submitting, setSubmitting] = useState(false);
    const { t } = useTranslation();

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
            toast.success(t("add_event.added"));
        } catch (err) {
            const message = err.response?.data?.message || t("add_event.error");
            toast.error(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Card className="p-4">
            <h3 className="font-semibold mb-4">{t("add_event.title")}</h3>
            <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-5 gap-3">
                <div>
                    <Label htmlFor="eventType" className="text-xs">
                        {t("add_event.type")}
                    </Label>
                    <Select value={eventType} onValueChange={setEventType}>
                        <SelectTrigger id="eventType">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            {EVENT_TYPES.map((opt) => (
                                <SelectItem key={opt.value} value={opt.value}>
                                    {opt.icon} {t(`event_type.${opt.value}`)}
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div>
                    <Label htmlFor="minute" className="text-xs">
                        {t("add_event.minute")}
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
                        {t("add_event.team")}
                    </Label>
                    <Select value={teamId} onValueChange={setTeamId}>
                        <SelectTrigger id="team">
                            <SelectValue placeholder="Select team" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value={String(match.homeTeam.id)}>
                                {match.homeTeam.shortName} {t("add_event.home_suffix")}
                            </SelectItem>
                            <SelectItem value={String(match.awayTeam.id)}>
                                {match.awayTeam.shortName} {t("add_event.away_suffix")}
                            </SelectItem>
                        </SelectContent>
                    </Select>
                </div>

                <div className="md:col-span-1">
                    <Label htmlFor="description" className="text-xs">
                        {t("add_event.description")}
                    </Label>
                    <Input
                        id="description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder={t("add_event.description_placeholder")}
                    />
                </div>

                <div className="flex items-end">
                    <Button
                        type="submit"
                        className="w-full"
                        disabled={submitting || !minute || !teamId}
                    >
                        <Plus className="h-4 w-4 mr-1" />
                        {submitting ? t("add_event.adding") : t("add_event.add")}
                    </Button>
                </div>
            </form>
        </Card>
    );
}