import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Toaster } from "@/components/ui/sonner";
import Layout from "@/components/Layout";
import ProtectedRoute from "@/components/ProtectedRoute";
import { AuthProvider } from "@/contexts/AuthContext";
import MatchListPage from "@/pages/MatchListPage";
import MatchDetailsPage from "@/pages/MatchDetailsPage";
import LoginPage from "@/pages/LoginPage";
import NotFoundPage from "@/pages/NotFoundPage";
import AdminPanelPage from "@/pages/admin/AdminPanelPage.jsx";
import TeamsAdminPage from "@/pages/admin/TeamsAdminPage";
import MatchesAdminPage from "@/pages/admin/MatchesAdminPage";

export default function App() {
    return (
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
                </Routes>
            </BrowserRouter>
            <Toaster richColors position="top-right" />
        </AuthProvider>
    );
}