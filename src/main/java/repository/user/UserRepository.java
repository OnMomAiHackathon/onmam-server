package repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import entity.user.OnmomUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<OnmomUser,Long> {
    boolean existsByEmail(String email);

    Optional<OnmomUser> findByEmail(String email);
}
