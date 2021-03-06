package task.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import task.notice.attachfile.domain.AttachFile;
import task.notice.attachfile.repository.AttachFileRepository;
import task.notice.common.jwt.JwtUtils;
import task.notice.helper.AttachFileTestHelper;
import task.notice.helper.NoticeTestHelper;
import task.notice.helper.UserTestHelper;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.request.SaveNoticeDto;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.repository.NoticeRepository;
import task.notice.user.domain.User;
import task.notice.user.repository.UserRepository;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class NoticeControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired UserRepository userRepository;
    @Autowired NoticeRepository noticeRepository;
    @Autowired AttachFileRepository attachFileRepository;
    @Autowired JwtUtils jwtUtils;
    @Autowired EntityManager em;

    ObjectMapper mapper;
    User testUser;
    AttachFile testAttachFile;
    String testToken1;
    String testToken2;
    Notice testNotice;

    @BeforeEach
    void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        testToken1 = generateToken("test1", "test1");
        generateTestNotice();
        generateTestAttachFile();
        testToken2 = generateToken("test2", "test2");
    }


    @DisplayName("???????????? ?????? ?????????")
    @Test
    void saveNoticeApiTest() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "test file1".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", MediaType.TEXT_PLAIN_VALUE, "test file2".getBytes(StandardCharsets.UTF_8) );

        String content = mapper.writeValueAsString(new SaveNoticeDto("title", "content", LocalDate.now().plusDays(7)));
        MockMultipartFile saveNotice = new MockMultipartFile("notice", "jsondata", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/notices")
                        .file(file1)
                        .file(file2)
                        .file(saveNotice)
                        .contentType(MediaType.MULTIPART_MIXED)
                        .accept(MediaType.APPLICATION_JSON) .characterEncoding("UTF-8")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken1)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("?????? ????????? - ???????????? ???????????? ??????")
    @Test
    void saveNoticeApiFailTest() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "test file1".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", MediaType.TEXT_PLAIN_VALUE, "test file2".getBytes(StandardCharsets.UTF_8) );

        String content = mapper.writeValueAsString(new SaveNoticeDto("title", "content", LocalDate.now().plusDays(7)));
        MockMultipartFile saveNotice = new MockMultipartFile("notice", "jsondata", MediaType.APPLICATION_JSON_VALUE, content.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/notices")
                        .file(file1)
                        .file(file2)
                        .file(saveNotice)
                        .contentType(MediaType.MULTIPART_MIXED)
                        .accept(MediaType.APPLICATION_JSON) .characterEncoding("UTF-8")
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @DisplayName("???????????? ?????? ?????? ?????????")
    @Test
    void findNoticeTest() throws Exception {
        mockMvc.perform(get("/notices/{noticeId}", testNotice.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("???????????? ?????? ?????? ?????????")
    @Test
    void findNoticeListTest() throws Exception {
        mockMvc.perform(get("/notices")
                .param("page", "0")
                .param("size", "10")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("???????????? ?????? ?????????")
    @Test
    void updateNoticeTest() throws Exception {
        UpdateNoticeDto updateNotice = new UpdateNoticeDto("updatedTitle", "updatedContent", LocalDate.now().plusDays(14));
        String updateNoticeJson = mapper.writeValueAsString(updateNotice);

        mockMvc.perform(put("/notices/{noticeId}", testNotice.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateNoticeJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken1)
        )
        .andDo(print())
        .andExpect(
                status().isOk()
        );
    }

    @DisplayName("???????????? ?????? ?????????")
    @Test
    void deleteNoticeTest() throws Exception {
        Long targetId = testNotice.getId();

        mockMvc.perform(delete("/notices/{noticeId}", targetId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken1)
        )
                .andDo(print())
                .andExpect(
                        status().isOk()
                );
    }

    @DisplayName("?????? ????????? - ????????? ????????? ????????? ?????? ????????? ?????? ?????????")
    @Test
    void updateNoticeOwnerMismatchTest() throws Exception {
        UpdateNoticeDto updateNotice = new UpdateNoticeDto("updatedTitle", "updatedContent", LocalDate.now().plusDays(14));
        String updateNoticeJson = mapper.writeValueAsString(updateNotice);
        Long targetId = testNotice.getId();

        mockMvc.perform(put("/notices/{noticeId}", targetId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateNoticeJson)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken2)
        )
                .andDo(print())
                .andExpect(
                        status().is4xxClientError()
                );
    }

    @DisplayName("?????? ????????? - ????????? ????????? ????????? ?????? ????????? ?????? ?????????")
    @Test
    void deleteNoticeOwnerMismatchTest() throws Exception {
        Long targetId = testNotice.getId();

        mockMvc.perform(delete("/notices/{noticeId}", targetId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken2)
                )
                .andDo(print())
                .andExpect(
                        status().is4xxClientError()
                );
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    void addAttachFileTest() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "addTest1.txt", MediaType.TEXT_PLAIN_VALUE, "add file1".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile file2 = new MockMultipartFile("file", "addTest2.txt", MediaType.TEXT_PLAIN_VALUE, "add file2".getBytes(StandardCharsets.UTF_8) );

        mockMvc.perform(multipart("/notices/{noticeId}/attach-file", testNotice.getId())
                        .file(file1)
                        .file(file2)
                        .contentType(MediaType.MULTIPART_MIXED)
                        .accept(MediaType.APPLICATION_JSON) .characterEncoding("UTF-8")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + testToken1)
                )
                .andDo(print())
                .andExpect(status().isOk());
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
