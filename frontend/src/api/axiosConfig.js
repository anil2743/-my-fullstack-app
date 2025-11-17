import axios from "axios";

// Detect environment and set API base URL
const getApiBaseUrl = () => {
  const hostname = window.location.hostname;
  const pathname = window.location.pathname;
  
  if (pathname.includes("/staging")) {
    // Staging environment
    return `//${hostname}/staging/api`;
  } else if (hostname === "65.1.231.237" || hostname === "localhost") {
    // Development or direct IP access
    return `//${hostname}:8080/api`;
  } else {
    // Production
    return "https://banking.cyetechnology.com/api";
  }
};

const api = axios.create({
  baseURL: getApiBaseUrl(),
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