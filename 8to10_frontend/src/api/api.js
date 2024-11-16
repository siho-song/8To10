import axios from 'axios';
import { buildAuthorizationHeader, parseBearerToken } from "@/utils/TokenUtils.js";
import CustomErrors from "@/api/CustomErrors.js";
import { HTTP_METHODS } from "@/constants/HttpMethods.js";
import {HTTP_STATUS} from "@/constants/HttpStatusCodes.js";


const api = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
});

const refreshAccessToken = async() => {

    try {
        const response = await api.post('/renew');
        return response.data['accessToken'];
    } catch (error) {
        console.error(error)
    }
}

api.interceptors.request.use((config) => {


    const accessToken = localStorage.getItem('Authorization');
    if(accessToken) {
        config.headers['Authorization'] = buildAuthorizationHeader(accessToken);
    }

    if ([HTTP_METHODS.POST, HTTP_METHODS.PUT, HTTP_METHODS.PATCH].includes(config.method)
        && config.data
        && Object.keys(config.data).length > 0) {
        config.headers['Content-Type'] = 'application/json';
    }

    if (config.url.includes('/renew')) {
        console.log("renew executed");
        config.withCredentials = true;
    }

    config.headers['Accept'] = 'application/json';

    return config;
}, (error) => {
    return Promise.reject(error);
});

api.interceptors.response.use((response) => {
    return response;
}, async (error) => {

    const { response } = error;
    const originalRequest = response.config;

    if (response && response.status === HTTP_STATUS.UNAUTHORIZED && !originalRequest._retry) {
        originalRequest._retry = true;

        try {
            const updatedAccessToken = await refreshAccessToken();

            localStorage.setItem('Authorization', updatedAccessToken);
            api.defaults.headers['Authorization'] = updatedAccessToken;
            response.config.headers['Authorization'] = updatedAccessToken;

            return api(response.config);
        } catch (refreshError) {
            // TODO 로그아웃
            localStorage.clear();
            console.log("직접 : ", refreshError);
            return Promise.reject(refreshError);
        }
    }

    if (response && response.data) {
        throw new CustomErrors(response.status, response.data.code, response.data.message);
    } else {
        throw new CustomErrors(0, 'NETWORK_ERROR', '네트워크 오류가 발생했습니다.');
    }
});

export default api;
