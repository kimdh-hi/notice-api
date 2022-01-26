package task.notice.dto.request;

public class SignupRequestDto {

    private String username;
    private String password;

    public SignupRequestDto() {
    }

    public SignupRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
