import type {AxiosInstance} from "axios";
import axios from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL;
if (!baseURL) {
  throw new Error("VITE_API_BASE_URL is not defined");
}

export const api: AxiosInstance = axios.create({
  baseURL,
  withCredentials: true,
  xsrfCookieName: "XSRF-TOKEN",
  xsrfHeaderName: "X-XSRF-TOKEN",
});

const getCookie = (name: string) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop()!.split(";").shift(); 
    return null;
};

api.interceptors.request.use((config) => {
    const token = getCookie("XSRF-TOKEN");
    if (token) {
        config.headers["X-XSRF-TOKEN"] = token;
    }
    return config;
});

export const getAxiosError = (err: unknown): string => {
    if (axios.isAxiosError(err)) {
        if (err.response) {
            const data: unknown = err.response.data;
            if (typeof data === 'string') return data;
            return "An unexpected error occurred.";
        } else if (err.request) {
            return "Network error. Please check your connection.";
        } else {
            return err.message ?? "An unexpected error occurred.";
        }
    }
    return "An unexpected error occurred.";
};

export default api;
