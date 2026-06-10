import { Badge } from "@/components/ui/badge";

export default function StatusBadge({ status }) {
    const variants = {
        LIVE: "bg-red-500 hover:bg-red-600 text-white",
        SCHEDULED: "bg-blue-500 hover:bg-blue-600 text-white",
        FINISHED: "bg-gray-500 hover:bg-gray-600 text-white",
        CANCELLED: "bg-gray-400 hover:bg-gray-500 text-white",
    };

    return (
        <Badge className={variants[status] || variants.SCHEDULED}>
            {status === "LIVE" && (
                <span className="mr-1 h-2 w-2 rounded-full bg-white animate-pulse" />
            )}
            {status}
        </Badge>
    );
}