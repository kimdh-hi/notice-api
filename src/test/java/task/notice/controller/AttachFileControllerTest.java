package task.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import task.notice.attachfile.domain.AttachFile;
import task.notice.attachfile.repository.AttachFileRepository;
import task.notice.common.jwt.JwtUtils;
import task.notice.helper.AttachFileTestHelper;
import task.notice.helper.NoticeTestHelper;
import task.notice.helper.UserTestHelper;
import task.notice.notice.domain.Notice;
import task.notice.notice.repository.NoticeRepository;
import task.notice.user.domain.User;
import task.notice.user.repository.UserRepository;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class AttachFileControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired UserRepository userRepository;
    @Autowired NoticeRepository noticeRepository;
    @Autowired AttachFileRepository attachFileRepository;
    @Autowired JwtUtils jwtUtils;
    @Autowired EntityManager em;

    ObjectMapper mapper;
    User testUser;
    String testToken;
    Notice testNotice;
    AttachFile testAttachFile;

    @BeforeEach
    void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        testToken = generateToken("test1", "test1");
        generateTestNotice();
        generateTestAttachFile();
    }

    @Test
    @DisplayName("첨부파일 삭제 테스트")
    void deleteAttachFileTest() throws Exception {
        System.out.println("attachFileId = " + testAttachFile.getId());
        mockMvc.perform(delete("/attach-file/{attachFileId}", testAttachFile.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken)
                )
                .andDo(print())
                .andExpect(status().isOk());

        testAttachFile = attachFileRepository.findById(testAttachFile.getId()).get();
        Assertions.assertTrue(testAttachFile.isDeleted());
    }

    private String generateToken(String username, String password) {
        testUser = UserTestHelper.createUser(username, password);
        userRepository.save(testUser);
        return jwtUtils.createToken(testUser.getUsername());
    }

    private void generateTestNotice() {
        testNotice = NoticeTestHelper.createNotice("testTitle", "testContent", 7, testUser);
        noticeRepository.save(testNotice);
    }

    private void generateTestAttachFile() {
        Notice notice = noticeRepository.findById(testNotice.getId()).get();
        testAttachFile = AttachFileTestHelper.createAttachFile("testFileName.txt", "testSaveName.txt");
        notice.setAttachFile(testAttachFile);
        em.flush();
        em.clear();
    }
}
