package task.notice.notice.repository;

import task.notice.notice.domain.Notice;

import java.util.Optional;

public interface NoticeQueryRepository {

    Notice findNotice(Long noticeId);

    boolean existsByUserId(Long noticeId, Long userId);
}
