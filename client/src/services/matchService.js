import api from "./api";

export const matchService = {
    async getAll(status = null) {
        const params = status ? { status } : {};
        const response = await api.get("/api/matches", { params });
        return response.data;
    },

    async getById(id) {
        const response = await api.get(`/api/matches/${id}`);
        return response.data;
    },

    async getEvents(matchId) {
        const response = await api.get(`/api/matches/${matchId}/events`);
        return response.data;
    },

    async create(data) {
        const response = await api.post("/api/matches", data);
        return response.data;
    },

    async delete(id) {
        await api.delete(`/api/matches/${id}`);
    },
};