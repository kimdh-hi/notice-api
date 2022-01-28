package task.notice.common.auth;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class TokenExtractor {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer ";

    public static  String extract(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (!Objects.isNull(authorization) && authorization.startsWith(TOKEN_TYPE)) {
            return authorization.substring(TOKEN_TYPE.length());
        }
        return null;
    }
}
