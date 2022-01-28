package task.notice.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import task.notice.notice.dto.request.SaveNoticeDto;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.dto.response.NoticeResponseDto;
import task.notice.notice.service.NoticeService;
import task.notice.user.domain.User;
import task.notice.user.domain.UserDetailsImpl;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity saveNotice(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam SaveNoticeDto saveNotice, @RequestParam(name = "files", required = false) List<MultipartFile> files) {

        log.info("saveNotice saveNotice={}", saveNotice);

        User user = userDetails.getUser();
        noticeService.saveNotice(saveNotice, files, user);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity findNotice(@PathVariable Long noticeId) {
        NoticeResponseDto noticeResponseDto = noticeService.findNotice(noticeId);

        return new ResponseEntity(noticeResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity updateNotice(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long noticeId, @RequestBody UpdateNoticeDto updateNotice) {

        User user = userDetails.getUser();
        noticeService.updateNotice(noticeId, updateNotice, user);

        return new ResponseEntity("공지사항 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity deleteNotice(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long noticeId
    ) {

        User user = userDetails.getUser();
        noticeService.deleteNotice(noticeId, user);

        return new ResponseEntity("공지사항 삭제 완료", HttpStatus.OK);
    }
}
