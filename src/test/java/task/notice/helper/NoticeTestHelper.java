package task.notice.helper;

import task.notice.notice.domain.Notice;
import task.notice.user.domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class NoticeTestHelper {

    public static Notice createNotice(String title, String content, int plusDays, User user) {
        return new Notice(title, content, LocalDate.now().plusDays(plusDays), user);
    }
}
