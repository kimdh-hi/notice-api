package task.notice.notice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AttachFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;
    @Column(nullable = false)
    private String saveFileName;

    @JoinColumn(name = "notice_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    public AttachFile(String originalFileName, String saveFileName) {
        this.originalFileName = originalFileName;
        this.saveFileName = saveFileName;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
