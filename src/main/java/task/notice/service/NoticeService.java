package task.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import task.notice.domain.AttachFile;
import task.notice.domain.Notice;
import task.notice.dto.request.SaveAttachFileDto;
import task.notice.dto.request.SaveNoticeDto;
import task.notice.repository.NoticeRepository;
import task.notice.utils.AwsS3Uploader;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final AwsS3Uploader s3Uploader;

    // 공지사항 등록
    public Long saveNotice(SaveNoticeDto dto, List<MultipartFile> multipartFiles) {
        // 파일 S3에 업도르 후 업로드 된 경로를 포함하는 경로 반환

        for(MultipartFile file: multipartFiles) {
            String uploadUrl = s3Uploader.upload(file);
            SaveAttachFileDto fileDto = new SaveAttachFileDto(file.getOriginalFilename(), uploadUrl);
            dto.addFile(fileDto);
        }

        // AttachFile, Notice 생성 및 저장
        List<AttachFile> attachFiles = dto.getAttachFiles().stream().map(
                file -> new AttachFile(file.getOriginalFileName(), file.getSaveFileName())
        ).collect(Collectors.toList());

        Notice notice = new Notice(dto.getTitle(), dto.getContent(), dto.getEndTime());
        attachFiles.stream().forEach(
                file -> notice.setAttachFile(file)
        );

        Notice savedNotice = noticeRepository.save(notice);

        return savedNotice.getId();
    }

    // 공지사항 목록조회

    // 공지사항 상세조회

    // 공지사항 수정

    // 공지사항 삭제
}
