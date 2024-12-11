import {HTTP_METHODS} from "@/constants/HttpMethods.js";


export const API_ENDPOINTS_METHOD = {
    LOGIN: HTTP_METHODS.POST,
    SIGNUP: HTTP_METHODS.POST,
    SIGNUP_EMAIL_EXISTS: HTTP_METHODS.GET,
    SIGNUP_NICKNAME_EXISTS: HTTP_METHODS.GET,
    LOGOUT: HTTP_METHODS.POST,

    RENEW: HTTP_METHODS.POST,

    GET_EVENTS: HTTP_METHODS.GET,

    DELETE_SCHEDULE: HTTP_METHODS.DELETE,

    CREATE_V_SCHEDULE: HTTP_METHODS.POST,
    EDIT_V_SCHEDULE: HTTP_METHODS.PUT,

    CREATE_F_SCHEDULE: HTTP_METHODS.POST,
    EDIT_F_SCHEDULE: HTTP_METHODS.PUT,

    EDIT_F_SCHEDULE_ITEM: HTTP_METHODS.PATCH,

    CREATE_N_SCHEDULE: HTTP_METHODS.POST,
    EDIT_N_SCHEDULE: HTTP_METHODS.PUT,
    PATCH_N_SCHEDULE_PROGRESS: HTTP_METHODS.PATCH,
    EDIT_N_SCHEDULE_ITEM: HTTP_METHODS.PUT,

    GET_BOARD_PAGING: HTTP_METHODS.GET,

    GET_POST: HTTP_METHODS.GET,
    CREATE_POST: HTTP_METHODS.POST,
    EDIT_POST: HTTP_METHODS.PUT,
    DELETE_POST: HTTP_METHODS.DELETE,

    CREATE_POST_LIKE: HTTP_METHODS.POST,
    DELETE_POST_LIKE: HTTP_METHODS.DELETE,

    CREATE_POST_SCRAP: HTTP_METHODS.POST,
    DELETE_POST_SCRAP: HTTP_METHODS.DELETE,

    CREATE_REPLY: HTTP_METHODS.POST,
    EDIT_REPLY: HTTP_METHODS.PUT,
    DELETE_REPLY: HTTP_METHODS.DELETE,

    CREATE_REPLY_LIKE: HTTP_METHODS.POST,
    DELETE_REPLY_LIKE: HTTP_METHODS.DELETE,

    GET_USER_STATSCARD: HTTP_METHODS.GET,

    GET_USER_PROFILE: HTTP_METHODS.GET,
    PUT_USER_NICKNAME: HTTP_METHODS.PUT,
    PUT_USER_PASSWORD: HTTP_METHODS.PUT,
    PUT_PROFILE_IMAGE: HTTP_METHODS.PUT,
    DELETE_PROFILE_IMAGE: HTTP_METHODS.DELETE,
    GET_MY_POSTS: HTTP_METHODS.GET,
    GET_MY_REPLIES: HTTP_METHODS.GET,
    GET_MY_SCRAPPED_BOARDS: HTTP_METHODS.GET,
};

export const API_ENDPOINT_NAMES = {
    LOGIN: "LOGIN",
    SIGNUP: "SIGNUP",
    SIGNUP_EMAIL_EXISTS: "SIGNUP_EMAIL_EXISTS",
    SIGNUP_NICKNAME_EXISTS: "SIGNUP_NICKNAME_EXISTS",
    LOGOUT: "LOGOUT",
    RENEW: "RENEW",
    GET_EVENTS: "GET_EVENTS",
    DELETE_SCHEDULE: "DELETE_SCHEDULE",
    CREATE_V_SCHEDULE: "CREATE_V_SCHEDULE",
    EDIT_V_SCHEDULE: "EDIT_V_SCHEDULE",
    CREATE_F_SCHEDULE: "CREATE_F_SCHEDULE",
    EDIT_F_SCHEDULE: "EDIT_F_SCHEDULE",
    EDIT_F_SCHEDULE_ITEM: "EDIT_F_SCHEDULE_ITEM",
    CREATE_N_SCHEDULE: "CREATE_N_SCHEDULE",
    EDIT_N_SCHEDULE: "EDIT_N_SCHEDULE",
    PATCH_N_SCHEDULE_PROGRESS: "PATCH_N_SCHEDULE_PROGRESS",
    EDIT_N_SCHEDULE_ITEM: "EDIT_N_SCHEDULE_ITEM",
    GET_BOARD_PAGING: "GET_BOARD_PAGING",
    GET_POST: "GET_POST",
    CREATE_POST: "CREATE_POST",
    EDIT_POST: "EDIT_POST",
    DELETE_POST: "DELETE_POST",
    CREATE_POST_LIKE: "CREATE_POST_LIKE",
    DELETE_POST_LIKE: "DELETE_POST_LIKE",
    CREATE_POST_SCRAP: "CREATE_POST_SCRAP",
    DELETE_POST_SCRAP: "DELETE_POST_SCRAP",
    CREATE_REPLY: "CREATE_REPLY",
    EDIT_REPLY: "EDIT_REPLY",
    DELETE_REPLY: "DELETE_REPLY",
    CREATE_REPLY_LIKE: "CREATE_REPLY_LIKE",
    DELETE_REPLY_LIKE: "DELETE_REPLY_LIKE",
    GET_USER_STATSCARD: "GET_USER_STATSCARD",
    GET_USER_PROFILE: "GET_USER_PROFILE",
    PUT_USER_NICKNAME: "PUT_USER_NICKNAME",
    PUT_USER_PASSWORD: "PUT_USER_PASSWORD",
    PUT_PROFILE_IMAGE: "PUT_PROFILE_IMAGE",
    DELETE_PROFILE_IMAGE: "DELETE_PROFILE_IMAGE",
    GET_MY_POSTS: "GET_MY_POSTS",
    GET_MY_REPLIES: "GET_MY_REPLIES",
    GET_MY_SCRAPPED_BOARDS: "GET_MY_SCRAPPED_BOARDS",
};

export const CORS_REQUIRED_ENDPOINTS = [
    API_ENDPOINT_NAMES.LOGIN,
    API_ENDPOINT_NAMES.RENEW,
    API_ENDPOINT_NAMES.LOGOUT,
];

export const URL_ENCODED_ENDPOINTS = [
    API_ENDPOINT_NAMES.LOGIN,
    API_ENDPOINT_NAMES.SIGNUP,
    API_ENDPOINT_NAMES.SIGNUP_EMAIL_EXISTS,
    API_ENDPOINT_NAMES.SIGNUP_NICKNAME_EXISTS,
]

export const JSON_ENDPOINTS = [
    API_ENDPOINT_NAMES.CREATE_V_SCHEDULE,
    API_ENDPOINT_NAMES.CREATE_F_SCHEDULE,
    API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,
    API_ENDPOINT_NAMES.EDIT_N_SCHEDULE_ITEM,
    API_ENDPOINT_NAMES.EDIT_F_SCHEDULE,
    API_ENDPOINT_NAMES.EDIT_N_SCHEDULE,
    API_ENDPOINT_NAMES.EDIT_V_SCHEDULE,
    API_ENDPOINT_NAMES.EDIT_POST,
    API_ENDPOINT_NAMES.CREATE_POST,
    API_ENDPOINT_NAMES.EDIT_REPLY,
    API_ENDPOINT_NAMES.CREATE_REPLY,
    API_ENDPOINT_NAMES.GET_USER_STATSCARD,
    API_ENDPOINT_NAMES.PATCH_N_SCHEDULE_PROGRESS,
    API_ENDPOINT_NAMES.PUT_USER_NICKNAME,
    API_ENDPOINT_NAMES.PUT_USER_PASSWORD,
    API_ENDPOINT_NAMES.EDIT_N_SCHEDULE_ITEM,
    API_ENDPOINT_NAMES.EDIT_F_SCHEDULE_ITEM,
];

export const MULTIPART_ENDPOINTS = [
    API_ENDPOINT_NAMES.PUT_PROFILE_IMAGE,
]

export const DEFAULT_URL = "http://35.241.28.118:80";