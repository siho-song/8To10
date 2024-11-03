package show.schedulemanagement.utils;

import io.jsonwebtoken.JwtException;

public class BearerAuthorizationUtils {
    public static final String BEARER_CODE = "Bearer ";

    public String extractToken(String authHeader){
        if(authHeader != null && authHeader.startsWith(BEARER_CODE)){
            return authHeader.substring(BEARER_CODE.length()).trim();
        }
        throw new JwtException("Invalid JWT Token");
    }
}
