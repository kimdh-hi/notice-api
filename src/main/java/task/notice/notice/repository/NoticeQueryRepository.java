package task.notice.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.response.NoticeResponseDto;

import java.util.List;
import java.util.Optional;

public interface NoticeQueryRepository {

    Notice findNotice(Long noticeId);

    Page<NoticeResponseDto> findNoticeList(Pageable pageable);

    Notice findNoticeAndAttachFiles(Long noticeId);

    boolean existsByUserId(Long noticeId, Long userId);
}
