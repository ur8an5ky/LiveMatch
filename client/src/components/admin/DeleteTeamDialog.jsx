import { useState } from "react";
import { toast } from "sonner";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { teamService } from "@/services/teamService";
import {useTranslation} from "react-i18next";

export default function DeleteTeamDialog({ open, onOpenChange, team, onDeleted }) {
    const [deleting, setDeleting] = useState(false);
    const { t } = useTranslation();

    const handleDelete = async () => {
        setDeleting(true);
        try {
            await teamService.delete(team.id);
            toast.success(t("team_delete.deleted", { name: team.name }));
            onDeleted(team.id);
            onOpenChange(false);
        } catch (err) {
            const message = err.response?.data?.message || t("team_delete.error");
            toast.error(message);
        } finally {
            setDeleting(false);
        }
    };

    return (
        <AlertDialog open={open} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>{t("team_delete.title")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("team_delete.description", { name: team?.name })}
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel disabled={deleting}>{t("common.cancel")}</AlertDialogCancel>
                    <AlertDialogAction
                        onClick={handleDelete}
                        disabled={deleting}
                        className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
                    >
                        {deleting ? t("common.deleting") : t("common.delete")}
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    );
}