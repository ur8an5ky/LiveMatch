import { useEffect, useState } from "react";
import { toast } from "sonner";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";
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
import { matchService } from "@/services/matchService";
import { teamService } from "@/services/teamService";

export default function MatchFormDialog({ open, onOpenChange, onSaved }) {
    const [teams, setTeams] = useState([]);
    const [homeTeamId, setHomeTeamId] = useState("");
    const [awayTeamId, setAwayTeamId] = useState("");
    const [startTime, setStartTime] = useState("");
    const [submitting, setSubmitting] = useState(false);

    // Load teams & reset form whenever dialog opens
    useEffect(() => {
        if (!open) return;

        teamService
            .getAll()
            .then(setTeams)
            .catch(() => toast.error("Could not load teams"));

        setHomeTeamId("");
        setAwayTeamId("");

        // Default: 1 hour from now, rounded to full hour
        const now = new Date();
        now.setHours(now.getHours() + 1);
        now.setMinutes(0);
        now.setSeconds(0);
        now.setMilliseconds(0);
        // Format for datetime-local: "YYYY-MM-DDTHH:mm"
        const tzOffsetMs = now.getTimezoneOffset() * 60 * 1000;
        const localIso = new Date(now.getTime() - tzOffsetMs)
            .toISOString()
            .slice(0, 16);
        setStartTime(localIso);
    }, [open]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (homeTeamId === awayTeamId) {
            toast.error("Home and away teams must be different");
            return;
        }

        setSubmitting(true);
        try {
            const data = {
                homeTeamId: Number(homeTeamId),
                awayTeamId: Number(awayTeamId),
                startTime: startTime + ":00",
            };
            const saved = await matchService.create(data);
            toast.success(
                `Match scheduled: ${saved.homeTeam.shortName} vs ${saved.awayTeam.shortName}`
            );
            onSaved(saved);
            onOpenChange(false);
        } catch (err) {
            const message = err.response?.data?.message || "Could not create match";
            toast.error(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>New match</DialogTitle>
                </DialogHeader>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <Label htmlFor="home">Home team</Label>
                        <Select value={homeTeamId} onValueChange={setHomeTeamId}>
                            <SelectTrigger id="home">
                                <SelectValue placeholder="Select home team" />
                            </SelectTrigger>
                            <SelectContent>
                                {teams.map((team) => (
                                    <SelectItem
                                        key={team.id}
                                        value={String(team.id)}
                                    >
                                        {team.name}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>

                    <div>
                        <Label htmlFor="away">Away team</Label>
                        <Select value={awayTeamId} onValueChange={setAwayTeamId}>
                            <SelectTrigger id="away">
                                <SelectValue placeholder="Select away team" />
                            </SelectTrigger>
                            <SelectContent>
                                {teams.map((team) => (
                                    <SelectItem
                                        key={team.id}
                                        value={String(team.id)}
                                        disabled={String(team.id) === homeTeamId}
                                    >
                                        {team.name}
                                    </SelectItem>
                                ))}
                            </SelectContent>
                        </Select>
                    </div>

                    <div>
                        <Label htmlFor="startTime">Start time</Label>
                        <Input
                            id="startTime"
                            type="datetime-local"
                            value={startTime}
                            onChange={(e) => setStartTime(e.target.value)}
                            required
                        />
                    </div>

                    <DialogFooter>
                        <Button
                            type="button"
                            variant="outline"
                            onClick={() => onOpenChange(false)}
                            disabled={submitting}
                        >
                            Cancel
                        </Button>
                        <Button
                            type="submit"
                            disabled={
                                submitting || !homeTeamId || !awayTeamId || !startTime
                            }
                        >
                            {submitting ? "Creating..." : "Create match"}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}