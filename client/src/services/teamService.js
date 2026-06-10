import api from "./api";

export const teamService = {
    async getAll() {
        const response = await api.get("/api/teams");
        return response.data;
    },

    async getById(id) {
        const response = await api.get(`/api/teams/${id}`);
        return response.data;
    },

    async create(data) {
        const response = await api.post("/api/teams", data);
        return response.data;
    },

    async update(id, data) {
        const response = await api.put(`/api/teams/${id}`, data);
        return response.data;
    },

    async delete(id) {
        await api.delete(`/api/teams/${id}`);
    },
};