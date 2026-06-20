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
import { matchService } from "@/services/matchService";
import {useTranslation} from "react-i18next";

export default function DeleteMatchDialog({ open, onOpenChange, match, onDeleted }) {
    const [deleting, setDeleting] = useState(false);
    const { t } = useTranslation();

    const handleDelete = async () => {
        setDeleting(true);
        try {
            await matchService.delete(match.id);
            toast.success(t("match_delete.deleted"));
            onDeleted(match.id);
            onOpenChange(false);
        } catch (err) {
            const message = err.response?.data?.message || t("match_delete.error");
            toast.error(message);
        } finally {
            setDeleting(false);
        }
    };

    return (
        <AlertDialog open={open} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>{t("match_delete.title")}</AlertDialogTitle>
                    <AlertDialogDescription>
                        {t("match_delete.description", {
                            home: match?.homeTeam?.name,
                            away: match?.awayTeam?.name,
                        })}
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