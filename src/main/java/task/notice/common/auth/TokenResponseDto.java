package task.notice.common.auth;

public class TokenResponseDto {

    private String token;

    public TokenResponseDto(String token) {
        this.token = token;
    }

    public static TokenResponseDto from(String token) {
        return new TokenResponseDto(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
