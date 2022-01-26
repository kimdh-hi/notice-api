package task.notice.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private final String JWT_SECRET = "secret";
    private final SignatureAlgorithm SIGNATURE = SignatureAlgorithm.HS256;
    private final JwtParser jwtParser = Jwts.parser().setSigningKey(JWT_SECRET);

    private long expirationTime = 1000 * 60 * 10;

    public String createToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SIGNATURE, JWT_SECRET)
                .compact();
    }

    public void validate(String token){
        try {
            jwtParser.parseClaimsJws(token);
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException  | InvalidClaimException ex) {
            throw ex;
        }
    }

    public String getSubject(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
}
