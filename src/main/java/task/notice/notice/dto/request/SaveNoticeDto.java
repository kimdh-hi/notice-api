package task.notice.notice.dto.request;

import task.notice.notice.domain.Notice;
import task.notice.user.domain.User;

import java.time.LocalDateTime;

public class SaveNoticeDto {

    private String title;
    private String content;
    private LocalDateTime endTime;

    public SaveNoticeDto() {
    }

    public Notice toEntity(User user) {
        return Notice.builder()
                .title(title)
                .content(content)
                .endTime(endTime)
                .user(user)
                .build();
    }

    public SaveNoticeDto(String title, String content, LocalDateTime endTime) {
        this.title = title;
        this.content = content;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
