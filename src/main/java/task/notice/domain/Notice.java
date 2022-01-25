package task.notice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttachFile> attachFiles = new ArrayList<>();

    public Notice(String title, String content, LocalDateTime endTime) {
        this.title = title;
        this.content = content;
        this.endTime = endTime;
        this.viewCount = 0L;
    }

    // 양방향 연관관계 편의 메서드
    public void setAttachFile(AttachFile file) {
        this.getAttachFiles().add(file);
        file.setNotice(this);
    }
}
