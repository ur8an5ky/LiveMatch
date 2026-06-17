import { Link } from "react-router-dom";
import {useTranslation} from "react-i18next";

export default function NotFoundPage() {
    const { t } = useTranslation();

    return (
        <div className="text-center py-12">
            <h1 className="text-4xl font-bold mb-4">404</h1>
            <p className="text-muted-foreground mb-4">{t("not_found.message")}</p>
            <Link to="/" className="text-primary hover:underline">
                {t("not_found.go_home")}
            </Link>
        </div>
    );
}