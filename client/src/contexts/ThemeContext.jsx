import { createContext, useContext, useEffect, useState } from "react";

const ThemeContext = createContext(null);

const STORAGE_KEY = "livematch-theme";

export function ThemeProvider({ children }) {
    const [theme, setThemeState] = useState(
        () => localStorage.getItem(STORAGE_KEY) || "light"
    );

    useEffect(() => {
        const root = document.documentElement;
        root.classList.remove("light", "dark");
        root.classList.add(theme);
    }, [theme]);

    const setTheme = (next) => {
        localStorage.setItem(STORAGE_KEY, next);
        setThemeState(next);
    };

    const toggle = () => setTheme(theme === "dark" ? "light" : "dark");

    return (
        <ThemeContext.Provider value={{ theme, setTheme, toggle }}>
            {children}
        </ThemeContext.Provider>
    );
}

export function useTheme() {
    const ctx = useContext(ThemeContext);
    if (!ctx) throw new Error("useTheme must be used within ThemeProvider");
    return ctx;
}