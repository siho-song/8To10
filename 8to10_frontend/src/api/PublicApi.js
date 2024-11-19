import axios from 'axios';
import CustomErrors from "@/api/CustomErrors.js";
import {setAcceptHeaders, setContentTypeHeaders, setWithCredentials} from "@/helpers/AxiosUtils.js";

const publicApi = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
});

publicApi.interceptors.request.use((config) => {
    setWithCredentials(config);

    setContentTypeHeaders(config);
    setAcceptHeaders(config);

    return config;
}, (error) => {
    return Promise.reject(error);
});

publicApi.interceptors.response.use((response) => {
    return response;
}, async (error) => {

    const { response } = error;

    if (response && response.data) {
        throw new CustomErrors(response.status, response.data.code, response.data.message);
    } else {
        throw new CustomErrors(0, 'NETWORK_ERROR', '네트워크 오류가 발생했습니다.');
    }
});

export default publicApi;
