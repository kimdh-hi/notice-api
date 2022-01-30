package task.notice.notice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import task.notice.attachfile.domain.AttachFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttachFileResponseDto {

    private String url;
    private String fileName;

    public static AttachFileResponseDto of(AttachFile attachFile) {
        return new AttachFileResponseDto(attachFile.getFileUrl(), attachFile.getOriginalFileName());
    }
}
