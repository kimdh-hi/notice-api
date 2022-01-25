package task.notice.repository;

import org.springframework.data.repository.CrudRepository;
import task.notice.domain.Notice;

public interface NoticeRepository extends CrudRepository<Notice, Long> {
}
