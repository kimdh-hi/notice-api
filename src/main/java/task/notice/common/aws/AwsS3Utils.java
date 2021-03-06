package task.notice.common.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import task.notice.attachfile.domain.AttachFile;
import task.notice.exception.exception.ExceptedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AwsS3Utils {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<AttachFile> upload(List<MultipartFile> files) {
        return files.stream().map(f -> new AttachFile(f.getOriginalFilename(), upload(f))).collect(Collectors.toList());
    }

    public void delete(String key) {
        s3Client.deleteObject(bucket, key);
    }

    private String upload(MultipartFile file) {
        File convertedFile = null;
        try {
            convertedFile = convert(file);
        } catch (IOException e) {
            throw new ExceptedException("파일 업로드에 실패했습니다.");
        }

        String extension = extractExtension(convertedFile.getName());
        String saveFileName = generateSaveFileName(extension);

        s3Client.putObject(bucket, saveFileName, convertedFile);
        removeNewFile(convertedFile);

        return s3Client.getUrl(bucket, saveFileName).toString();

    }

    private File convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(file.getBytes());
        }

        return convertFile;
    }

    private String generateSaveFileName(String extension) {
        return UUID.randomUUID().toString() + extension;
    }

    private String extractExtension(String fileName) {
        int idx = fileName.lastIndexOf(".");
        if (idx > 0) {
            return fileName.substring(idx);
        } else {
            return "";
        }
    }

    private void removeNewFile(File targetFile) {
        if(!Objects.isNull(targetFile)) {
            targetFile.delete();
        }
    }
}
