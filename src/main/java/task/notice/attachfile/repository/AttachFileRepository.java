package task.notice.attachfile.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import task.notice.attachfile.domain.AttachFile;

import java.util.List;

public interface AttachFileRepository extends CrudRepository<AttachFile, Long> {

    @Query("select f from AttachFile f where f.deleted = ?1")
    List<AttachFile> findByDeleted(Boolean deleted);
}
