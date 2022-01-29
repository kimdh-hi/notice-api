package task.notice.notice.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateNoticeDto {

    private String title;
    private String content;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime endTime;

}
