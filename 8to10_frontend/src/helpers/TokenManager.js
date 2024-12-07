import authenticatedApi from "@/api/AuthenticatedApi.js";
import {API_ENDPOINT_NAMES} from "@/constants/ApiEndPoints.js";

export const parseBearerToken = (token) => {
    if (!token) return null;

    const bearerRegex = /^Bearer\s+(.+)$/;
    const match = token.match(bearerRegex);
    return match ? match[1] : null;
};

export const buildBearerToken = (token) => {
    if (!token) return null;
    return 'Bearer ' + token;
};


export const refreshAccessToken = async() => {
    try {
        const response = await authenticatedApi.post(
            '/renew',
            {},
            {apiEndPoint: API_ENDPOINT_NAMES.RENEW});
        return response.data['accessToken'];
    } catch (error) {
        console.error(error.toString());
        console.error(error);
        throw error;
    }
}