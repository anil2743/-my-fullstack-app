import axios from 'axios';

const api = axios.create({
    baseURL: window.location.pathname.startsWith("/staging")
        ? "/staging/api"
        : "/api",
});

// Interceptor to add JWT to every request
api.interceptors.request.use(config => {
    const token = localStorage.getItem('authToken');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

export default api;
