package task.notice.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import task.notice.attachfile.domain.AttachFile;
import task.notice.attachfile.repository.AttachFileRepository;
import task.notice.common.aws.AwsS3Utils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeletedAttachFileScheduler {

    private final AwsS3Utils s3Utils;
    private final AttachFileRepository attachFileRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void job() {
        List<AttachFile> deletedAttachFiles = attachFileRepository.findByDeleted(true);
        log.info("DeletedAttachFileScheduler delete file size = {}", deletedAttachFiles.size());
        deletedAttachFiles.forEach(
                f -> {
                    s3Utils.delete(f.getSaveFileName());
                    attachFileRepository.deleteById(f.getId());
                }
        );
    }
}
