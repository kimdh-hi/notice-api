package task.notice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import task.notice.attachfile.domain.AttachFile;
import task.notice.attachfile.repository.AttachFileRepository;
import task.notice.config.AppConfig;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(AppConfig.class)
@DataJpaTest
public class AttachFileRepositoryTest {

    @Autowired AttachFileRepository attachFileRepository;
    @Autowired EntityManager em;

    @DisplayName("삭제처리 된 첨부파일만 조회")
    @Test
    void findDeletedAttachFileTest() {
        //given
        AttachFile attachFile1 = new AttachFile("test1", "test1");
        attachFile1.delete();
        AttachFile attachFile2 = new AttachFile("test2", "test2");
        attachFile2.delete();
        AttachFile attachFile3 = new AttachFile("test3", "test3");
        attachFileRepository.saveAll(List.of(attachFile1, attachFile2, attachFile3));
        em.flush(); em.clear();
        //when
        List<AttachFile> deletedFile = attachFileRepository.findByDeleted(true);
        //then
        assertEquals(2, deletedFile.size());
        assertEquals("test1", deletedFile.get(0).getOriginalFileName());
        assertEquals("test2", deletedFile.get(1).getOriginalFileName());
    }
}
