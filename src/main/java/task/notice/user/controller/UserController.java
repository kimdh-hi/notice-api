package task.notice.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.notice.common.auth.TokenResponseDto;
import task.notice.user.dto.request.LoginRequestDto;
import task.notice.user.dto.request.SignupRequestDto;
import task.notice.user.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupRequestDto requestDto) {
        userService.saveUser(requestDto);

        return new ResponseEntity("회원가입 성공", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto requestDto) {
        TokenResponseDto tokenResponseDto = userService.login(requestDto);
        log.info("jwt={}", tokenResponseDto.getToken());
        return new ResponseEntity(tokenResponseDto, HttpStatus.OK);
    }
}
