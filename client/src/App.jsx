import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "@/components/Layout";
import MatchListPage from "@/pages/MatchListPage";
import MatchDetailsPage from "@/pages/MatchDetailsPage";
import LoginPage from "@/pages/LoginPage";
import AdminPanelPage from "@/pages/AdminPanelPage";
import NotFoundPage from "@/pages/NotFoundPage";

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route index element={<MatchListPage />} />
            <Route path="matches/:id" element={<MatchDetailsPage />} />
            <Route path="login" element={<LoginPage />} />
            <Route path="admin" element={<AdminPanelPage />} />
            <Route path="*" element={<NotFoundPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
  );
}