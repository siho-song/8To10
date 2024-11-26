import axios from 'axios';
import CustomErrors from "@/api/CustomErrors.js";
import {HTTP_STATUS} from "@/constants/HttpStatusCodes.js";
import {
    setAcceptHeaders,
    setAuthorizationHeaders,
    setContentTypeHeaders,
    setWithCredentials
} from "@/helpers/AxiosConfig.js";
import {refreshAccessToken} from "@/helpers/TokenManager.js";

const authenticatedApi = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
});


authenticatedApi.interceptors.request.use((config) => {

    setWithCredentials(config);

    setAuthorizationHeaders(config);
    setContentTypeHeaders(config);
    setAcceptHeaders(config);

    return config;
}, (error) => {
    return Promise.reject(error);
});

authenticatedApi.interceptors.response.use((response) => {
    return response;
}, async (error) => {

    const { response } = error;
    const originalRequest = response.config;

    if (response && response.status === HTTP_STATUS.UNAUTHORIZED && !originalRequest._retry) {
        originalRequest._retry = true;

        try {
            const updatedAccessToken = await refreshAccessToken();

            localStorage.setItem('Authorization', updatedAccessToken);
            response.config.headers['Authorization'] = updatedAccessToken;

            return authenticatedApi(response.config);
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

export default authenticatedApi;
