package task.notice.notice.dto.request;

import task.notice.notice.domain.AttachFile;

public class SaveAttachFileDto {

    private String originalFileName;
    private String saveFileName;

    public AttachFile toEntity() {
        return new AttachFile(originalFileName, saveFileName);
    }

    public SaveAttachFileDto(String originalFileName, String saveFileName) {
        this.originalFileName = originalFileName;
        this.saveFileName = saveFileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }
}
