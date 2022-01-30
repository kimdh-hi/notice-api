package task.notice.attachfile.dto.request;

public class AddAttachFileDto {

    private String originalFileName;
    private String saveFileName;

    public AddAttachFileDto() {
    }

    public AddAttachFileDto(String originalFileName, String saveFileName) {
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
