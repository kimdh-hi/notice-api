package task.notice.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import task.notice.domain.Notice;

import java.time.LocalDateTime;

public class NoticeResponseDto {

    private String title;
    private String content;
    private LocalDateTime startTime;
    private Long viewCount;
    private String writer;

    public NoticeResponseDto(String title, String content, LocalDateTime startTime, Long viewCount, String writer) {
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.viewCount = viewCount;
        this.writer = writer;
    }

    public static NoticeResponseDto fromEntity(Notice notice) {
        return null;
    }
}
