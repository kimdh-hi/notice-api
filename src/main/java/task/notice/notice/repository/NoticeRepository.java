package task.notice.notice.repository;

import org.springframework.data.repository.CrudRepository;
import task.notice.notice.domain.Notice;

public interface NoticeRepository extends CrudRepository<Notice, Long>, NoticeQueryRepository {

    
}
