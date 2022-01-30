package task.notice.common.aws;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import task.notice.attachfile.domain.AttachFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
public class AwsS3UtilsTest {

    @Autowired
    private AwsS3Utils awsS3Utils;

    @DisplayName("파일 업로드 테스트")
    @Test
    void fileUploaderTest() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8));
        List<AttachFile> attachFiles = awsS3Utils.upload(List.of(multipartFile));

        Assertions.assertNotNull(attachFiles);
        Assertions.assertEquals("test.txt", attachFiles.get(0).getOriginalFileName());
    }

    @DisplayName("파일 삭제 테스트")
    @Test
    void fileDeleteTest() {
        MultipartFile multipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8));
        List<AttachFile> attachFiles = awsS3Utils.upload(List.of(multipartFile));

        String saveFileName = attachFiles.get(0).getSaveFileName();
        System.out.println("saveFileName="+saveFileName);
        awsS3Utils.delete(saveFileName);

    }
}