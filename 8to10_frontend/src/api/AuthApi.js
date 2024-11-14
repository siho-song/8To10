import axios from 'axios';
import CustomErrors from "@/api/CustomErrors.js";
import { HTTP_METHODS } from "@/constants/HttpMethods.js";


const authApi = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
});

authApi.interceptors.request.use((config) => {

    if (config.url.includes('/login')) {
        config.headers['Content-Type'] = 'application/x-www-form-urlencoded';
        config.withCredentials = true;
    } else if ([HTTP_METHODS.POST, HTTP_METHODS.PUT, HTTP_METHODS.PATCH].includes(config.method)
        && config.data
        && Object.keys(config.data).length > 0) {
        config.headers['Content-Type'] = 'application/json';
    }
    config.headers['Accept'] = 'application/json';

    return config;
}, (error) => {
    return Promise.reject(error);
});

authApi.interceptors.response.use((response) => {
    return response;
}, async (error) => {

    const { response } = error;

    if (response && response.data) {
        throw new CustomErrors(response.status, response.data.code, response.data.message);
    } else {
        throw new CustomErrors(0, 'NETWORK_ERROR', '네트워크 오류가 발생했습니다.');
    }
});

export default authApi;
