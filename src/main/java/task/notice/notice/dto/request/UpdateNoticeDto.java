package task.notice.notice.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UpdateNoticeDto {

    private String title;
    private String content;
    private LocalDateTime endTime;

    public UpdateNoticeDto(String title, String content, LocalDateTime endTime) {
        this.title = title;
        this.content = content;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
