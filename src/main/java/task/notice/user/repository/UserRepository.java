package task.notice.user.repository;

import org.springframework.data.repository.CrudRepository;
import task.notice.user.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
