package task.notice.helper;

import task.notice.notice.domain.Notice;
import task.notice.user.domain.User;

import java.time.LocalDateTime;

public class NoticeTestHelper {

    public static Notice createNotice(String title, String content, int plusDays, User user) {
        return new Notice(title, content, LocalDateTime.now().plusDays(plusDays), user);
    }
}
