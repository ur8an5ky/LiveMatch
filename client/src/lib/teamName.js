import { useTranslation } from "react-i18next";

/**
 * Returns a function that maps an ISO/custom country code to its localized name.
 * Falls back to the raw code if no translation is found.
 */
export function useCountryName() {
    const { t } = useTranslation();
    return (code) => {
        if (!code) return "";
        return t(`country.${code}`, code); // 2nd arg = fallback
    };
}

/**
 * Returns a function that gets a team's display name.
 * - Club team: returns team.name (e.g. "FC Barcelona")
 * - National team (name is null/empty): returns localized country name
 */
export function useTeamName() {
    const { t } = useTranslation();
    return (team) => {
        if (!team) return "";
        if (!team.name) {
            return t(`country.${team.country}`, team.country);
        }
        return team.name;
    };
}