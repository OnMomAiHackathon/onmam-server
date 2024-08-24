package repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import entity.user.OnmomUser;

public interface UserRepository extends JpaRepository<OnmomUser,Long> {
    boolean existsByEmail(String email);
}
