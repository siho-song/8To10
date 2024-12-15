import axios from 'axios';
import CustomErrors from "@/api/CustomErrors.js";
import {setAcceptHeaders, setContentTypeHeaders, setWithCredentials} from "@/helpers/AxiosHelper.js";
import {DEFAULT_URL} from "@/constants/ApiEndPoints.js";

const publicApi = axios.create({
    baseURL: DEFAULT_URL,
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
        throw new CustomErrors(0, 'NETWORK_ERROR', "네트워크 연결이 불안정합니다. 인터넷 연결을 확인하고 다시 시도해 주세요.");
    }
});

export default publicApi;
