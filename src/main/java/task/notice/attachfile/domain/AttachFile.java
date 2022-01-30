package task.notice.attachfile.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import task.notice.notice.domain.Notice;

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
    private String fileUrl;

    @JoinColumn(name = "notice_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    private boolean deleted;

    public AttachFile(String originalFileName, String fileUrl) {
        this.originalFileName = originalFileName;
        this.fileUrl = fileUrl;
        this.deleted = false;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void delete() {
        this.deleted = true;
    }

    public String getSaveFileName() {
        int idx = fileUrl.lastIndexOf("/");
        return fileUrl.substring(idx+1);
    }
}
