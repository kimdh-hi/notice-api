package task.notice.helper;

import task.notice.attachfile.domain.AttachFile;
import task.notice.notice.domain.Notice;

public class AttachFileTestHelper {

    public static AttachFile createAttachFile(String originalName, String saveName) {
        AttachFile attachFile = new AttachFile(originalName, saveName);

        return attachFile;
    }
}
