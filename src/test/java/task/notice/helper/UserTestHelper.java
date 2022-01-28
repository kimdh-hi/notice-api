package task.notice.helper;

import task.notice.user.domain.User;

public class UserTestHelper {

    public static User createUser(String username, String password){
        return new User(username, password);
    }
}
