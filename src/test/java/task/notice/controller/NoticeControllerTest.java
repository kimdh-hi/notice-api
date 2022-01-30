package task.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import task.notice.common.jwt.JwtUtils;
import task.notice.helper.NoticeTestHelper;
import task.notice.helper.UserTestHelper;
import task.notice.notice.domain.Notice;
import task.notice.notice.dto.request.SaveNoticeDto;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.notice.repository.NoticeRepository;
import task.notice.user.domain.User;
import task.notice.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
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
    @Autowired JwtUtils jwtUtils;

    ObjectMapper mapper;
    User testUser;
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
        testToken2 = generateToken("test2", "test2");
    }


    @DisplayName("공지사항 등록 테스트")
    @Test
    void saveNoticeApiTest() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "test file1".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", MediaType.TEXT_PLAIN_VALUE, "test file2".getBytes(StandardCharsets.UTF_8) );

        String content = mapper.writeValueAsString(new SaveNoticeDto("title", "content", LocalDateTime.now().plusDays(7)));
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

    @DisplayName("실패 테스트 - 토큰없이 공지사항 등록")
    @Test
    void saveNoticeApiFailTest() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.txt", MediaType.TEXT_PLAIN_VALUE, "test file1".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.txt", MediaType.TEXT_PLAIN_VALUE, "test file2".getBytes(StandardCharsets.UTF_8) );

        String content = mapper.writeValueAsString(new SaveNoticeDto("title", "content", LocalDateTime.now().plusDays(7)));
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

    @DisplayName("공지사항 단건 조회 테스트")
    @Test
    void findNoticeTest() throws Exception {
        mockMvc.perform(get("/notices/{noticeId}", testNotice.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("공지사항 수정 테스트")
    @Test
    void updateNoticeTest() throws Exception {
        UpdateNoticeDto updateNotice = new UpdateNoticeDto("updatedTitle", "updatedContent", LocalDateTime.now().plusDays(14));
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

    @DisplayName("공지사항 삭제 테스트")
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

    @DisplayName("실패 테스트 - 자신이 등록한 공지가 아닌 공지를 수정 테스트")
    @Test
    void updateNoticeOwnerMismatchTest() throws Exception {
        UpdateNoticeDto updateNotice = new UpdateNoticeDto("updatedTitle", "updatedContent", LocalDateTime.now().plusDays(14));
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

    @DisplayName("실패 테스트 - 자신이 등록한 공지가 아닌 공지를 삭제 테스트")
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
    @DisplayName("첨부파일 추가 테스트")
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
}
