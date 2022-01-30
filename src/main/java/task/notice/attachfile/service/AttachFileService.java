package task.notice.attachfile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import task.notice.attachfile.domain.AttachFile;
import task.notice.attachfile.repository.AttachFileRepository;
import task.notice.common.aws.AwsS3Utils;
import task.notice.exception.exception.OwnerMismatchException;
import task.notice.notice.domain.Notice;
import task.notice.notice.repository.NoticeRepository;
import task.notice.user.domain.User;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AttachFileService {

    private final NoticeRepository noticeRepository;
    private final AttachFileRepository attachFileRepository;
    private final AwsS3Utils s3Utils;

    @Transactional
    public void deleteAttachFile(Long attachFileId, User user) {
        AttachFile attachFile = attachFileRepository.findById(attachFileId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 첨부파일입니다."));
        attachFile.delete();
    }

    @Transactional
    public void addAttachFile(Long noticeId, List<MultipartFile> files, User user) {
        checkNoticeOwner(noticeId, user, OwnerMismatchException.UPDATE_MISMATCH);

        List<AttachFile> attachFiles = s3Utils.upload(files);
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));
        attachFiles.forEach(notice::setAttachFile);
    }

    private void checkNoticeOwner(Long noticeId, User user, String message) {
        if (!noticeRepository.existsByUserId(noticeId, user.getId())) {
            throw new OwnerMismatchException(message);
        }
    }
}
