package task.notice.notice.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import task.notice.notice.domain.Notice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
public class NoticeResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDate endDate;
    private Long viewCount;
    private String writer;

    private List<AttachFileResponseDto> files = new ArrayList<>();

    @QueryProjection
    public NoticeResponseDto(Long id, String title, String content, LocalDate endDate, Long viewCount, String writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.endDate = endDate;
        this.viewCount = viewCount;
        this.writer = writer;
    }

    public NoticeResponseDto(Long id, String title, String content, LocalDate endDate, Long viewCount, String writer, List<AttachFileResponseDto> files) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.endDate = endDate;
        this.viewCount = viewCount;
        this.writer = writer;
        this.files = files;
    }

    public static NoticeResponseDto of(Notice notice) {
        return new NoticeResponseDto(notice.getId(), notice.getTitle(), notice.getContent(), notice.getEndDate(), notice.getViewCount(), notice.getUser().getUsername());
    }

    public static NoticeResponseDto of(Notice notice, List<AttachFileResponseDto> files) {
        return new NoticeResponseDto(notice.getId(), notice.getTitle(), notice.getContent(), notice.getEndDate(), notice.getViewCount(), notice.getUser().getUsername(), files);
    }
}
