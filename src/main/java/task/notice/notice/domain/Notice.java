package task.notice.notice.domain;

import lombok.*;
import task.notice.attachfile.domain.AttachFile;
import task.notice.common.domain.Timestamp;
import task.notice.user.domain.User;
import task.notice.notice.dto.request.UpdateNoticeDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDateTime endTime;

    private Long viewCount;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttachFile> attachFiles = new ArrayList<>();

    public Notice(String title, String content, LocalDateTime endTime, User user) {
        this.title = title;
        this.content = content;
        this.endTime = endTime;
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
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.endTime = dto.getEndTime();
    }
}
