package task.notice.common.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import task.notice.common.utils.JwtUtils;
import task.notice.user.domain.User;
import task.notice.user.domain.UserDetailsImpl;
import task.notice.user.repository.UserRepository;
import task.notice.user.service.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticateFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isPreflightRequest(request)) filterChain.doFilter(request, response);

        String token = TokenExtractor.extract(request);
        try {
            if (!Objects.isNull(token) && !"".equals(token) && jwtUtils.validate(token)) {
                User user = extractUser(token);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user), null, AuthorityUtils.NO_AUTHORITIES);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }

    private User extractUser(String token) {
        Long userId = Long.valueOf(jwtUtils.getSubject(token));
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        });
    }

    private boolean isPreflightRequest(HttpServletRequest request) {
        return request.getMethod().equals(HttpMethod.OPTIONS.toString());
    }
}
