package task.notice.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import task.notice.notice.dto.request.SaveNoticeDto;
import task.notice.notice.dto.request.SaveNoticeTestDto;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.dto.response.NoticeResponseDto;
import task.notice.notice.service.NoticeService;
import task.notice.user.domain.User;
import task.notice.user.domain.UserDetailsImpl;

import java.time.LocalDate;
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
            @RequestParam String title, @RequestParam String content,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam LocalDate endDate,
            @RequestParam(value = "file", required = false) List<MultipartFile> file) {

        SaveNoticeDto saveNoticeDto = new SaveNoticeDto(title, content, endDate);

        User user = userDetails.getUser();
        noticeService.saveNotice(saveNoticeDto, file, user);

        return new ResponseEntity("공지사항 등록에 성공했습니다.", HttpStatus.OK);
    }

    @GetMapping(value = "/{noticeId}")
    public ResponseEntity findNotice(@PathVariable Long noticeId) {
        NoticeResponseDto noticeResponseDto = noticeService.findNotice(noticeId);

        return ResponseEntity.ok(noticeResponseDto);
    }

    @GetMapping
    public ResponseEntity findNoticeList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<NoticeResponseDto> noticeList = noticeService.findNoticeList(page, size);

        return ResponseEntity.ok(noticeList);
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

    //첨부파일 추가
    @PostMapping("/{noticeId}/attach-file")
    public ResponseEntity addAttachFile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) List<MultipartFile> file,
            @PathVariable Long noticeId) {

        User user = userDetails.getUser();
        noticeService.addAttachFile(noticeId, file, user);
        int count = file.size();
        return new ResponseEntity(count + "건 첨부파일 추가", HttpStatus.OK);
    }
}
