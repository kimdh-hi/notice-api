package task.notice.utils;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    JwtUtils jwtUtils;
    String username = "test@gmail.com";

    @BeforeEach
    void init() {
        jwtUtils = new JwtUtils();
    }

    // 토큰 발급 테스트
    @DisplayName("토큰 발급 테스트")
    @Test
    void createToken() {
        String token = jwtUtils.createToken(username);

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);
    }

    // 토큰 검증 테스트
    @DisplayName("토큰 검증 테스트")
    @Test
    void validateToken() {
        String token = jwtUtils.createToken(username);

        jwtUtils.validate(token);
    }

    // 토큰 검증 실패 테스트 (유효기간이 지난 경우)
    @DisplayName("토큰 검증 실패 테스트 (유효기간이 지난 경우)")
    @Test
    void validateFailTest1() throws InterruptedException {
        jwtUtils.setExpirationTime(1000);

        String token = jwtUtils.createToken(username);
        Thread.sleep(1200);

        assertThrows(ExpiredJwtException.class, () -> jwtUtils.validate(token));
    }

    // 토큰 검증 실패 테스트 (변조된 토큰)
    @DisplayName("토큰 검증 실패 테스트 (토큰이 변조된 경우)")
    @Test
    void validateFailTest2() {
        String token = jwtUtils.createToken(username);
        String manipulatedToken = token.concat("a");

        assertNotEquals(token, manipulatedToken);
        assertThrows(RuntimeException.class, () -> jwtUtils.validate(manipulatedToken));
    }
}
