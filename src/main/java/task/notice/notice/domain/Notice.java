package task.notice.notice.domain;

import lombok.*;
import task.notice.attachfile.domain.AttachFile;
import task.notice.common.domain.Timestamp;
import task.notice.notice.dto.request.UpdateNoticeDto;
import task.notice.user.domain.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notice extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Long viewCount;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<AttachFile> attachFiles = new ArrayList<>();

    public Notice(String title, String content, LocalDate endDate, User user) {
        this.title = title;
        this.content = content;
        this.endDate = endDate;
        this.viewCount = 0L;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // 양방향 연관관계 편의 메서드
    public void setAttachFile(AttachFile file) {
        this.getAttachFiles().add(file);
        file.setNotice(this);
    }

    // == 도메인 메서드 ==//
    public void view() {
        this.viewCount++;
    }

    // 수정
    public void update(UpdateNoticeDto dto) {
        if (!Objects.isNull(dto.getTitle())) this.title = dto.getTitle();
        if (!Objects.isNull(dto.getContent())) this.content = dto.getContent();
        if (!Objects.isNull(dto.getEndDate())) this.endDate = dto.getEndDate();
    }
}
