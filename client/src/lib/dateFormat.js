import { format as formatFn } from "date-fns";
import { pl, enUS } from "date-fns/locale";
import { useTranslation } from "react-i18next";

const LOCALES = {
    pl: pl,
    en: enUS,
};

export function useDateFormat() {
    const { i18n } = useTranslation();

    return (date, pattern = "dd MMM yyyy, HH:mm") => {
        const locale = LOCALES[i18n.language] || enUS;
        return formatFn(date, pattern, { locale });
    };
}