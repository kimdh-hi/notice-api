package task.notice.service;

import antlr.Token;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.notice.domain.User;
import task.notice.dto.request.LoginRequestDto;
import task.notice.dto.request.SignupRequestDto;
import task.notice.dto.response.TokenResponseDto;
import task.notice.repository.UserRepository;
import task.notice.security.principal.UserDetailsImpl;
import task.notice.utils.JwtUtils;

@Transactional(readOnly = false)
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public User saveUser(SignupRequestDto requestDto) {
        String encodedPassword = encode(requestDto.getPassword());
        User user = new User(requestDto.getUsername(), encodedPassword);

        return userRepository.save(user);
    }

    public TokenResponseDto login(LoginRequestDto requestDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("로그인에 실패했습니다.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(requestDto.getUsername());
        String token = jwtUtils.createToken(userDetails.getUsername());

        return TokenResponseDto.from(token);
    }


    private String encode(String password) {
        return passwordEncoder.encode(password);
    }
}
