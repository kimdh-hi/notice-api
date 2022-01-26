package task.notice.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AwsS3Uploader {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        File convertedFile = null;
        try {
            convertedFile = convert(file);
        } catch (IOException e) {
            e.printStackTrace();
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
