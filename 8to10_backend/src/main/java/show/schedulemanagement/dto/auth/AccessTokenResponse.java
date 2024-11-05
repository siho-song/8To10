package show.schedulemanagement.dto.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AccessTokenResponse {
    private String accessToken;

    public static AccessTokenResponse from(String accessToken){
        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.accessToken = accessToken;
        return accessTokenResponse;
    }
}