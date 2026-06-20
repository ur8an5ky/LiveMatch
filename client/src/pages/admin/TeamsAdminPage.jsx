import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Plus, Pencil, Trash2 } from "lucide-react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Skeleton } from "@/components/ui/skeleton";
import TeamFormDialog from "@/components/admin/TeamFormDialog";
import DeleteTeamDialog from "@/components/admin/DeleteTeamDialog";
import { teamService } from "@/services/teamService";
import {useTranslation} from "react-i18next";

export default function TeamsAdminPage() {
    const [teams, setTeams] = useState([]);
    const [loading, setLoading] = useState(true);

    const [editingTeam, setEditingTeam] = useState(null);   // null = no dialog, undefined = create, object = edit
    const [deletingTeam, setDeletingTeam] = useState(null); // null = no dialog, object = confirm
    const { t } = useTranslation();

    useEffect(() => {
        teamService
            .getAll()
            .then(setTeams)
            .catch(() => toast.error(t("teams_admin.load_error")))
            .finally(() => setLoading(false));
    }, []);

    const handleSaved = (savedTeam) => {
        setTeams((prev) => {
            const exists = prev.some((t) => t.id === savedTeam.id);
            if (exists) {
                return prev.map((t) => (t.id === savedTeam.id ? savedTeam : t));
            }
            return [...prev, savedTeam];
        });
    };

    const handleDeleted = (deletedId) => {
        setTeams((prev) => prev.filter((t) => t.id !== deletedId));
    };

    return (
        <div>
            <div className="mb-4">
                <Link
                    to="/admin"
                    className="text-sm text-muted-foreground hover:text-foreground"
                >
                    {t("admin.back")}
                </Link>
            </div>

            <div className="flex items-center justify-between mb-6">
                <h1 className="text-3xl font-bold">{t("teams_admin.title")}</h1>
                <Button onClick={() => setEditingTeam(undefined)}>
                    <Plus className="h-4 w-4 mr-2" />
                    {t("teams_admin.new")}
                </Button>
            </div>

            {loading ? (
                <div className="space-y-2">
                    {[1, 2, 3].map((i) => (
                        <Skeleton key={i} className="h-12 w-full" />
                    ))}
                </div>
            ) : teams.length === 0 ? (
                <p className="text-muted-foreground text-center py-12">
                    {t("teams_admin.empty")}
                </p>
            ) : (
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>{t("teams_admin.col_name")}</TableHead>
                            <TableHead>{t("teams_admin.col_short_name")}</TableHead>
                            <TableHead>{t("teams_admin.col_country")}</TableHead>
                            <TableHead className="text-right">{t("common.actions")}</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {teams.map((team) => (
                            <TableRow key={team.id}>
                                <TableCell className="font-medium">
                                    {team.name}
                                </TableCell>
                                <TableCell>{team.shortName}</TableCell>
                                <TableCell>{team.country}</TableCell>
                                <TableCell className="text-right">
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        onClick={() => setEditingTeam(team)}
                                    >
                                        <Pencil className="h-4 w-4" />
                                    </Button>
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        onClick={() => setDeletingTeam(team)}
                                        className="text-destructive hover:text-destructive"
                                    >
                                        <Trash2 className="h-4 w-4" />
                                    </Button>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            )}

            <TeamFormDialog
                open={editingTeam !== null}
                onOpenChange={(open) => !open && setEditingTeam(null)}
                team={editingTeam || null}
                onSaved={handleSaved}
            />

            <DeleteTeamDialog
                open={deletingTeam !== null}
                onOpenChange={(open) => !open && setDeletingTeam(null)}
                team={deletingTeam}
                onDeleted={handleDeleted}
            />
        </div>
    );
}