package task.notice.common.utils;

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
        // given
        String token = jwtUtils.createToken(username);
        // when, then
        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);
    }

    // 토큰 검증 테스트
    @DisplayName("토큰 검증 테스트")
    @Test
    void validateToken() {
        // given
        String token = jwtUtils.createToken(username);
        // when, then
        jwtUtils.validate(token);
    }

    // 토큰에서 subject 추출 테스트
    @DisplayName("토큰 subject 추출 테스트")
    @Test
    void extractSubject() {
        // given
        String token = jwtUtils.createToken(username);
        // when
        String subject = jwtUtils.getSubject(token);
        // then
        assertEquals(username, subject);
    }

    // 토큰 검증 실패 테스트 (유효기간이 지난 경우)
    @DisplayName("토큰 검증 실패 테스트 (유효기간이 지난 경우)")
    @Test
    void validateFailTest1() throws InterruptedException {
        // given
        jwtUtils.setExpirationTime(1000);
        String token = jwtUtils.createToken(username);
        // when
        Thread.sleep(1200);
        // then
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.validate(token));
    }

    // 토큰 검증 실패 테스트 (변조된 토큰)
    @DisplayName("토큰 검증 실패 테스트 (토큰이 변조된 경우)")
    @Test
    void validateFailTest2() {
        // given
        String token = jwtUtils.createToken(username);
        // when
        String manipulatedToken = token.concat("a");
        // then
        assertNotEquals(token, manipulatedToken);
        assertThrows(RuntimeException.class, () -> jwtUtils.validate(manipulatedToken));
    }
}
