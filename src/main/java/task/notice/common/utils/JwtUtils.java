package task.notice.common.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private final String JWT_SECRET = "secret";
    private final SignatureAlgorithm SIGNATURE = SignatureAlgorithm.HS256;
    private final JwtParser jwtParser = Jwts.parser().setSigningKey(JWT_SECRET);

    private long expirationTime = 1000 * 60 * 10;

    public String createToken(String subject) {

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SIGNATURE, JWT_SECRET)
                .compact();
    }

    public boolean validate(String token) {
        log.info("jwtUtils validate 호출");
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            boolean result = !claims.getBody().getExpiration().before(new Date());
            log.info("jwtUtils validate 성공 {}", result);
            return result;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("jwtUtils validate 실패");
            return false;
        }
    }

    public String getSubject(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

}
