import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Toaster } from "@/components/ui/sonner";
import Layout from "@/components/Layout";
import ProtectedRoute from "@/components/ProtectedRoute";
import { AuthProvider } from "@/contexts/AuthContext";
import { ThemeProvider } from "@/contexts/ThemeContext";
import MatchListPage from "@/pages/MatchListPage";
import MatchDetailsPage from "@/pages/MatchDetailsPage";
import LoginPage from "@/pages/LoginPage";
import NotFoundPage from "@/pages/NotFoundPage";
import AdminPanelPage from "@/pages/admin/AdminPanelPage.jsx";
import TeamsAdminPage from "@/pages/admin/TeamsAdminPage";
import MatchesAdminPage from "@/pages/admin/MatchesAdminPage";
import MatchControlPage from "@/pages/admin/MatchControlPage";

export default function App() {
    return (
        <ThemeProvider>
            <AuthProvider>
                <BrowserRouter>
                    <Routes>
                        <Route element={<Layout />}>
                            <Route index element={<MatchListPage />} />
                            <Route path="matches/:id" element={<MatchDetailsPage />} />
                            <Route path="login" element={<LoginPage />} />
                            <Route
                                path="admin"
                                element={
                                    <ProtectedRoute requireAdmin>
                                        <AdminPanelPage />
                                    </ProtectedRoute>
                                }
                            />
                            <Route
                                path="admin/teams"
                                element={
                                    <ProtectedRoute requireAdmin>
                                        <TeamsAdminPage />
                                    </ProtectedRoute>
                                }
                            />
                            <Route path="*" element={<NotFoundPage />} />
                        </Route>
                        <Route
                            path="admin/matches"
                            element={
                                <ProtectedRoute requireAdmin>
                                    <MatchesAdminPage />
                                </ProtectedRoute>
                            }
                        />
                        <Route
                            path="admin/matches/:id/control"
                            element={
                                <ProtectedRoute requireAdmin>
                                    <MatchControlPage />
                                </ProtectedRoute>
                            }
                        />
                    </Routes>
                </BrowserRouter>
                <Toaster
                    richColors
                    position="top-center"
                    expand
                    duration={8000}
                    toastOptions={{
                        style: {
                            minWidth: '420px',
                            fontSize: '0.95rem',
                            padding: '14px 16px',
                            textAlign: 'center',
                        },
                        classNames: {
                            title: 'w-full text-center',
                            description: 'w-full text-center',
                            content: 'w-full text-center',
                        },
                    }}
                />
            </AuthProvider>
        </ThemeProvider>
    );
}