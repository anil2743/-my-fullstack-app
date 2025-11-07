import axios from 'axios';

const base =
    window.location.href.includes("/staging")
        ? "http://65.1.231.237/staging/api"
        : "http://65.1.231.237/api";

const api = axios.create({
    baseURL: base,
});

// Attach JWT
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("authToken");
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    },
    (error) => Promise.reject(error)
);

export default api;
