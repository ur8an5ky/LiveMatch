import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Users, Trophy, Activity } from "lucide-react";
import { Card } from "@/components/ui/card";
import { teamService } from "@/services/teamService";
import { matchService } from "@/services/matchService";

export default function AdminPanelPage() {
    const [stats, setStats] = useState({ teams: 0, matches: 0, live: 0 });

    useEffect(() => {
        Promise.all([teamService.getAll(), matchService.getAll()])
            .then(([teams, matches]) => {
                setStats({
                    teams: teams.length,
                    matches: matches.length,
                    live: matches.filter((m) => m.status === "LIVE").length,
                });
            })
            .catch(() => {
                // silently — dashboard cards will just show 0
            });
    }, []);

    return (
        <div>
            <h1 className="text-3xl font-bold mb-6">Admin panel</h1>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
                <StatCard
                    icon={<Users className="h-8 w-8 text-primary" />}
                    label="Teams"
                    value={stats.teams}
                />
                <StatCard
                    icon={<Trophy className="h-8 w-8 text-primary" />}
                    label="Matches"
                    value={stats.matches}
                />
                <StatCard
                    icon={<Activity className="h-8 w-8 text-red-500" />}
                    label="Live now"
                    value={stats.live}
                />
            </div>

            <h2 className="text-xl font-semibold mb-4">Manage</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <Link to="/admin/teams">
                    <Card className="p-6 hover:shadow-md transition-shadow cursor-pointer">
                        <div className="flex items-center gap-3 mb-2">
                            <Users className="h-6 w-6 text-primary" />
                            <h3 className="text-lg font-semibold">Teams</h3>
                        </div>
                        <p className="text-sm text-muted-foreground">
                            Create, edit and remove football teams.
                        </p>
                    </Card>
                </Link>

                <Card className="p-6 opacity-60 cursor-not-allowed">
                    <div className="flex items-center gap-3 mb-2">
                        <Trophy className="h-6 w-6 text-primary" />
                        <h3 className="text-lg font-semibold">
                            Matches (coming next)
                        </h3>
                    </div>
                    <p className="text-sm text-muted-foreground">
                        Schedule matches and control live events.
                    </p>
                </Card>
            </div>
        </div>
    );
}

function StatCard({ icon, label, value }) {
    return (
        <Card className="p-6">
            <div className="flex items-center gap-4">
                {icon}
                <div>
                    <div className="text-3xl font-bold tabular-nums">{value}</div>
                    <div className="text-sm text-muted-foreground">{label}</div>
                </div>
            </div>
        </Card>
    );
}