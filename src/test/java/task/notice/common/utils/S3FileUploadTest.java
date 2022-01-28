package task.notice.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class S3FileUploadTest {

    @Autowired
    private AwsS3Uploader uploader;

    @DisplayName("파일 업로드 테스트")
    @Test
    void fileUploaderTest() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8));
        uploader.upload(multipartFile);
    }
}