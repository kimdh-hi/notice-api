package task.notice.repository;

import org.springframework.data.repository.CrudRepository;
import task.notice.domain.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
