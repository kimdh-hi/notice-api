package task.notice.dto.response;

public class TokenResponseDto {

    private String token;

    public TokenResponseDto(String token) {
        this.token = token;
    }

    public static TokenResponseDto from(String token) {
        return new TokenResponseDto(token);
    }
}
