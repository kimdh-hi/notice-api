package task.notice.dto.request;

import task.notice.domain.Notice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaveNoticeDto {

    private String title;
    private String content;
    private LocalDateTime endTime;

    private List<SaveAttachFileDto> attachFiles = new ArrayList<>();

    public Notice toEntity() {
        Notice notice = new Notice(title, content, endTime);
        attachFiles.stream().map(SaveAttachFileDto::toEntity).forEach(notice::setAttachFile);
        return notice;
    }

    public void addFile(SaveAttachFileDto dto) {
        this.attachFiles.add(dto);
    }

    public SaveNoticeDto() {
    }

    public SaveNoticeDto(String title, String content, LocalDateTime endTime) {
        this.title = title;
        this.content = content;
        this.endTime = endTime;
    }

    public SaveNoticeDto(String title, String content, LocalDateTime endTime, List<SaveAttachFileDto> attachFiles) {
        this.title = title;
        this.content = content;
        this.endTime = endTime;
        this.attachFiles = attachFiles;
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

    public List<SaveAttachFileDto> getAttachFiles() {
        return attachFiles;
    }

    public void setAttachFiles(List<SaveAttachFileDto> attachFiles) {
        this.attachFiles = attachFiles;
    }
}
