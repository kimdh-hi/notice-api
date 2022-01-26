package task.notice.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import task.notice.domain.AttachFile;
import task.notice.domain.Notice;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NoticeRepositoryTest {

    @Autowired NoticeRepository noticeRepository;
    @Autowired EntityManager em;

    @DisplayName("공지사항 생성")
    @Test
    void createNoticeTest() {
        // given
        AttachFile file1 = new AttachFile("testOriginalFileName1.txt", "testSavedFileName1.txt");
        AttachFile file2 = new AttachFile("testOriginalFileName2.txt", "testSavedFileName2.txt");
        Notice notice = new Notice("제목", "내용", LocalDateTime.now().plusDays(7), null);
        notice.setAttachFile(file1);
        notice.setAttachFile(file2);

        // when
        Notice savedNotice = noticeRepository.save(notice);

        // then
        assertNotNull(savedNotice.getId());
        assertEquals(notice.getTitle(), savedNotice.getTitle());
        assertEquals(notice.getContent(), savedNotice.getContent());
        assertEquals(notice.getEndTime(), savedNotice.getEndTime());
        assertEquals(0, savedNotice.getViewCount());

        assertEquals(2, savedNotice.getAttachFiles().size());
    }

    @Test
    @DisplayName("공지사항 조회")
    void readNoticeTest() {
        // given
        AttachFile file1 = new AttachFile("testOriginalFileName1.txt", "testSavedFileName1.txt");
        AttachFile file2 = new AttachFile("testOriginalFileName2.txt", "testSavedFileName2.txt");
        Notice notice = new Notice("제목", "내용", LocalDateTime.now().plusDays(7), null);
        notice.setAttachFile(file1);
        notice.setAttachFile(file2);
        Notice savedNotice = noticeRepository.save(notice);

        // when
        em.flush();
        em.clear();
        Notice findNotice = noticeRepository.findById(savedNotice.getId()).get();

        // then
        assertEquals(notice.getTitle(), findNotice.getTitle());
        assertEquals(notice.getContent(), findNotice.getContent());
        assertNotNull(findNotice.getEndTime());
        assertEquals(notice.getViewCount(), findNotice.getViewCount());
        assertEquals(2, notice.getAttachFiles().size());
        assertEquals(file1.getOriginalFileName(), savedNotice.getAttachFiles().get(0).getOriginalFileName());
        assertEquals(file1.getSaveFileName(), savedNotice.getAttachFiles().get(0).getSaveFileName());
    }

    @Test
    @DisplayName("공지사항 조회 - 지연로딩 테스트")
    void readNoticeLazyLoadingTest() {
        // given
        AttachFile file1 = new AttachFile("testOriginalFileName1.txt", "testSavedFileName1.txt");
        AttachFile file2 = new AttachFile("testOriginalFileName2.txt", "testSavedFileName2.txt");
        Notice notice = new Notice("제목", "내용", LocalDateTime.now().plusDays(7), null);
        notice.setAttachFile(file1);
        notice.setAttachFile(file2);
        Notice savedNotice = noticeRepository.save(notice);

        // when
        em.flush();
        em.clear();
        Notice findNotice = noticeRepository.findById(savedNotice.getId()).get();

        // then
        assertEquals(notice.getTitle(), findNotice.getTitle());
        assertEquals(notice.getContent(), findNotice.getContent());
        assertNotNull(findNotice.getEndTime());
        assertEquals(notice.getViewCount(), findNotice.getViewCount());
    }
}
