package task.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import task.notice.domain.AttachFile;
import task.notice.domain.Notice;
import task.notice.dto.request.SaveAttachFileDto;
import task.notice.dto.request.SaveNoticeDto;
import task.notice.repository.NoticeRepository;
import task.notice.utils.AwsS3Uploader;

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
    public Long saveNotice(SaveNoticeDto saveDto, List<MultipartFile> multipartFiles) {

        // 파일 S3에 업도르 후 업로드 된 경로를 포함하는 경로 반환
        upload(saveDto, multipartFiles);

        // AttachFile 생성
        Notice notice = saveDto.toEntity();

        Notice savedNotice = noticeRepository.save(notice);

        return savedNotice.getId();
    }

    // 공지사항 목록조회

    // 공지사항 상세조회

    // 공지사항 수정

    // 공지사항 삭제


    private void upload(SaveNoticeDto noticeDto, List<MultipartFile> files) {
        for(MultipartFile file: files) {
            String uploadUrl = s3Uploader.upload(file);
            SaveAttachFileDto fileDto = new SaveAttachFileDto(file.getOriginalFilename(), uploadUrl);
            noticeDto.addFile(fileDto);
        }
    }
}
