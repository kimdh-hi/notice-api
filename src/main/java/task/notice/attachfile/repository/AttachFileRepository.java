package task.notice.attachfile.repository;

import org.springframework.data.repository.CrudRepository;
import task.notice.attachfile.domain.AttachFile;

public interface AttachFileRepository extends CrudRepository<AttachFile, Long> {
}
