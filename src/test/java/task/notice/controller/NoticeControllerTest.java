package task.notice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import task.notice.dto.request.SaveNoticeDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class NoticeControllerTest {

    @Autowired
    MockMvc mockMvc;
    ObjectMapper mapper;

    @BeforeAll
    void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @DisplayName("POST /notices 공지사항 등록 API 테스트")
    @Test
    void saveNoticeApiTest() throws Exception {
        MockMultipartFile multipartFile1 = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
        MockMultipartFile multipartFile2 = new MockMultipartFile("file", "test2.txt", "text/plain", "test file2".getBytes(StandardCharsets.UTF_8) );

        SaveNoticeDto noticeDto = new SaveNoticeDto("title1", "content1", LocalDateTime.now().plusDays(7));
        String noticeDtoJson = mapper.writeValueAsString(noticeDto);
        MockMultipartFile notice = new MockMultipartFile("notice", "notice", "application/json", noticeDtoJson.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/notices")
                        .file(multipartFile1)
                        .file(multipartFile2)
                        .file(notice)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }
}
