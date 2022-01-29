package task.notice.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import task.notice.notice.domain.AttachFile;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.request.SaveAttachFileDto;
import task.notice.notice.dto.request.SaveNoticeDto;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.dto.response.NoticeResponseDto;
import task.notice.notice.repository.NoticeRepository;
import task.notice.common.utils.AwsS3Uploader;
import task.notice.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AwsS3Uploader s3Uploader;

    // 공지사항 등록
    @Transactional
    public Long saveNotice(SaveNoticeDto saveDto, List<MultipartFile> files, User user) {

        // 파일 S3에 업도르 후 업로드 된 경로를 포함하는 경로 반환
        List<AttachFile> attachFiles = upload(files);
        // AttachFile 생성
        Notice notice = new Notice(saveDto.getTitle(), saveDto.getContent(), saveDto.getEndTime(), user);
        attachFiles.forEach(notice::setAttachFile);

        Notice savedNotice = noticeRepository.save(notice);

        return savedNotice.getId();
    }

    // 공지사항 상세조회
    public NoticeResponseDto findNotice(Long noticeId) {
        Notice notice = noticeRepository.findNotice(noticeId);
        return NoticeResponseDto.fromEntity(notice);
    }

    // 공지사항 수정
    @Transactional
    public void updateNotice(Long noticeId, UpdateNoticeDto updateDto, User user) {
        isMyNotice(noticeId, user, "공지를 수정할 권한이 없습니다.");

        Notice notice = getNotice(noticeId);
        notice.update(updateDto);
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long noticeId, User user) {
        isMyNotice(noticeId, user, "공지를 삭제할 권한이 없습니다.");

        noticeRepository.deleteById(noticeId);
    }

    // 공지사항 목록조회

    private Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> {
            throw new IllegalArgumentException("존재하지 않는 공지사항 입니다.");
        });
    }

    private List<AttachFile> upload(List<MultipartFile> files) {
        return files.stream().map(f -> new AttachFile(f.getOriginalFilename(), s3Uploader.upload(f))).collect(Collectors.toList());
    }

    private void isMyNotice(Long noticeId, User user, String message) {
       if (!noticeRepository.existsByUserId(noticeId, user.getId())) {
           throw new AccessDeniedException(message);
       }
    }
}
