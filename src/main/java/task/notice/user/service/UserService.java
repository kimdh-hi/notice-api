package task.notice.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.notice.user.domain.User;
import task.notice.user.dto.request.LoginRequestDto;
import task.notice.user.dto.request.SignupRequestDto;
import task.notice.common.jwt.TokenResponseDto;
import task.notice.user.repository.UserRepository;
import task.notice.common.jwt.JwtUtils;

@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(SignupRequestDto requestDto) {
        User user = new User(requestDto.getUsername(), passwordEncoder.encode(requestDto.getPassword()));

        return userRepository.save(user);
    }

    public TokenResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 ID 입니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀립니다.");
        }

        String token = jwtUtils.createToken(requestDto.getUsername());

        return TokenResponseDto.from(token);
    }
}
