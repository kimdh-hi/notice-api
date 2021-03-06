package task.notice.notice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import task.notice.attachfile.repository.AttachFileRepository;
import task.notice.exception.exception.OwnerMismatchException;
import task.notice.attachfile.domain.AttachFile;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.request.SaveNoticeDto;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.dto.response.AttachFileResponseDto;
import task.notice.notice.dto.response.NoticeResponseDto;
import task.notice.notice.repository.NoticeRepository;
import task.notice.common.aws.AwsS3Utils;
import task.notice.user.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AttachFileRepository attachFileRepository;
    private final AwsS3Utils s3Utils;

    // 공지사항 등록
    @Transactional
    public Long saveNotice(SaveNoticeDto saveDto, List<MultipartFile> files, User user) {

        // 파일 S3에 업도르 후 업로드 된 경로를 포함하는 경로 반환
        List<AttachFile> attachFiles = s3Utils.upload(files);
        // AttachFile 생성
        Notice notice = new Notice(saveDto.getTitle(), saveDto.getContent(), saveDto.getEndDate(), user);
        attachFiles.forEach(notice::setAttachFile);

        Notice savedNotice = noticeRepository.save(notice);

        return savedNotice.getId();
    }

    // 공지사항 상세조회
    public NoticeResponseDto findNotice(Long noticeId) {
        Notice notice = noticeRepository.findNotice(noticeId);
        notice.view();
        List<AttachFileResponseDto> attachFilesDto = notice.getAttachFiles().stream().map(AttachFileResponseDto::of).collect(Collectors.toList());
        return NoticeResponseDto.of(notice, attachFilesDto);
    }

    // 공지사항 목록조회
    public Page<NoticeResponseDto> findNoticeList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NoticeResponseDto> noticeList = noticeRepository.findNoticeList(pageable);

        return noticeList;
    }


    // 공지사항 수정
    @Transactional
    public void updateNotice(Long noticeId, UpdateNoticeDto updateDto, User user) {
        checkNoticeOwner(noticeId, user, OwnerMismatchException.UPDATE_MISMATCH);

        Notice notice = getNotice(noticeId);
        notice.update(updateDto);
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long noticeId, User user) {
        checkNoticeOwner(noticeId, user, OwnerMismatchException.DELETE_MISMATCH);

        List<AttachFile> attachFiles = noticeRepository.findNoticeAndAttachFiles(noticeId).getAttachFiles();
        log.info("deleteNotice attachFile size = {}", attachFiles.size());
        attachFiles.forEach(AttachFile::delete);

        noticeRepository.deleteById(noticeId);
    }

    @Transactional
    public void addAttachFile(Long noticeId, List<MultipartFile> files, User user) {
        checkNoticeOwner(noticeId, user, OwnerMismatchException.UPDATE_MISMATCH);

        List<AttachFile> attachFiles = s3Utils.upload(files);
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 공지사항입니다."));
        attachFiles.forEach(notice::setAttachFile);
    }


    private Notice getNotice(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> {
            throw new IllegalArgumentException("존재하지 않는 공지사항 입니다.");
        });
    }

    private void checkNoticeOwner(Long noticeId, User user, String message) {
        if (!noticeRepository.existsByUserId(noticeId, user.getId())) {
           throw new OwnerMismatchException(message);
       }
    }
}
