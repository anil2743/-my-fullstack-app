import axios from "axios";

// Use the domain for production
const base = "https://banking.cyetechnology.com/api";

const api = axios.create({
    baseURL: base,
});

// Attach JWT token automatically
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("authToken");
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => Promise.reject(error)
);

export default api;