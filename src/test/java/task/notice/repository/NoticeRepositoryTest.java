package task.notice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import task.notice.config.AppConfig;
import task.notice.helper.NoticeTestHelper;
import task.notice.helper.UserTestHelper;
import task.notice.attachfile.domain.AttachFile;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.repository.NoticeRepository;
import task.notice.user.domain.User;
import task.notice.user.repository.UserRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@Import({AppConfig.class})
@DataJpaTest
public class NoticeRepositoryTest {

    @Autowired UserRepository userRepository;
    @Autowired NoticeRepository noticeRepository;
    @Autowired EntityManager em;

    User user;
    Notice notice;
    AttachFile file1;
    AttachFile file2;

    @BeforeEach
    void init() {
        user = UserTestHelper.createUser("testUsername", "testPassword");
        userRepository.save(user);
        file1 = new AttachFile("testOriginalFileName1.txt", "testSavedFileName1.txt");
        file2 = new AttachFile("testOriginalFileName2.txt", "testSavedFileName2.txt");
        notice = NoticeTestHelper.createNotice("testTitle", "testContent", 7, user);
        notice.setAttachFile(file1);
        notice.setAttachFile(file2);
        noticeRepository.save(notice);
    }

    @DisplayName("공지사항 생성")
    @Test
    void createNoticeTest() {
        // given
        AttachFile file1 = new AttachFile("testOriginalFileName1.txt", "testSavedFileName1.txt");
        AttachFile file2 = new AttachFile("testOriginalFileName2.txt", "testSavedFileName2.txt");
        Notice notice = new Notice("제목", "내용", LocalDateTime.now().plusDays(7), user);
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
        // when
        Notice findNotice = noticeRepository.findById(notice.getId()).get();

        // then
        assertEquals(notice.getTitle(), findNotice.getTitle());
        assertEquals(notice.getContent(), findNotice.getContent());
        assertNotNull(findNotice.getEndTime());
        assertEquals(notice.getViewCount(), findNotice.getViewCount());
        assertEquals(2, findNotice.getAttachFiles().size());
        assertEquals(file1.getOriginalFileName(), findNotice.getAttachFiles().get(0).getOriginalFileName());
        assertEquals(file1.getSaveFileName(), findNotice.getAttachFiles().get(0).getSaveFileName());
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

    @Test
    @DisplayName("공지사항 수정")
    void updateNoticeTest(){
        //given
        String updateTitle = "updatedTitle";
        String updateContent = "updateContent";
        LocalDateTime updateEndTime = LocalDateTime.now();
        UpdateNoticeDto updateNoticeDto = new UpdateNoticeDto(updateTitle, updateContent, updateEndTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //when
        Notice notice = noticeRepository.findById(this.notice.getId()).get();
        notice.update(updateNoticeDto);
        em.flush();
        em.clear();
        notice = noticeRepository.findById(this.notice.getId()).get();
        //then
        assertEquals(updateTitle, notice.getTitle());
        assertEquals(updateContent, notice.getContent());
        assertEquals(updateEndTime.format(formatter), notice.getEndTime().format(formatter));
    }
}
