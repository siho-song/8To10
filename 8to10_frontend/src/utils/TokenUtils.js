export const parseBearerToken = (token) => {

    if (!token) return null;

    const bearerRegex = /^Bearer\s+(.+)$/;
    const match = token.match(bearerRegex);
    return match ? match[1] : null;
};

export const buildAuthorizationHeader = (token) => {
    if (!token) return null;
    return 'Bearer ' + token;
};