package task.notice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import task.notice.dto.request.SaveNoticeDto;
import task.notice.service.NoticeService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity saveNotice(
            @RequestPart("notice") SaveNoticeDto saveNotice, @RequestPart(name = "file", required = false) List<MultipartFile> files)
    {
            noticeService.saveNotice(saveNotice, files);

            return new ResponseEntity(HttpStatus.CREATED);
    }
}
