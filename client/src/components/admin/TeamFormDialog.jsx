import { useEffect, useState } from "react";
import { toast } from "sonner";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { teamService } from "@/services/teamService";

/**
 * Reusable team form dialog for create and edit.
 * Pass `team` to edit, omit for create.
 */
export default function TeamFormDialog({ open, onOpenChange, team, onSaved }) {
    const [name, setName] = useState("");
    const [shortName, setShortName] = useState("");
    const [country, setCountry] = useState("");
    const [submitting, setSubmitting] = useState(false);

    const isEdit = !!team;

    // Pre-fill fields when editing
    useEffect(() => {
        if (team) {
            setName(team.name || "");
            setShortName(team.shortName || "");
            setCountry(team.country || "");
        } else {
            setName("");
            setShortName("");
            setCountry("");
        }
    }, [team, open]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSubmitting(true);
        try {
            const data = { name, shortName, country };
            const saved = isEdit
                ? await teamService.update(team.id, data)
                : await teamService.create(data);
            toast.success(isEdit ? "Team updated" : "Team created");
            onSaved(saved);
            onOpenChange(false);
        } catch (err) {
            const message = err.response?.data?.message || "Operation failed";
            toast.error(message);
        } finally {
            setSubmitting(false);
        }
    };

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>
                        {isEdit ? "Edit team" : "New team"}
                    </DialogTitle>
                </DialogHeader>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <Label htmlFor="name">Name</Label>
                        <Input
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            placeholder="FC Barcelona"
                            required
                            autoFocus
                        />
                    </div>
                    <div>
                        <Label htmlFor="shortName">Short name</Label>
                        <Input
                            id="shortName"
                            value={shortName}
                            onChange={(e) => setShortName(e.target.value)}
                            placeholder="FCB"
                            maxLength={10}
                            required
                        />
                    </div>
                    <div>
                        <Label htmlFor="country">Country</Label>
                        <Input
                            id="country"
                            value={country}
                            onChange={(e) => setCountry(e.target.value)}
                            placeholder="Spain"
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
                        <Button type="submit" disabled={submitting}>
                            {submitting
                                ? "Saving..."
                                : isEdit
                                    ? "Save changes"
                                    : "Create team"}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
}