package task.notice.attachfile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import task.notice.attachfile.service.AttachFileService;
import task.notice.user.domain.User;
import task.notice.user.domain.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attach-file")
public class AttachFileController {

    private final AttachFileService attachFileService;

    //첨부파일 삭제
    @DeleteMapping("/{attachFileId}")
    public ResponseEntity deleteAttachFile(
            @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long attachFileId) {

        User user = userDetails.getUser();
        attachFileService.deleteAttachFile(attachFileId, user);
        return new ResponseEntity("첨부파일 삭제 완료", HttpStatus.OK);
    }
}