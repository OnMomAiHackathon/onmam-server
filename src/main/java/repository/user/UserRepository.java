package repository.user;

import entity.group.OnmomGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import entity.user.OnmomUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<OnmomUser,Long> {
    boolean existsByEmail(String email);

    Optional<OnmomUser> findByEmail(String email);

    List<OnmomUser> findByGroup(OnmomGroup group);

    Optional<OnmomUser> findByKakaoId(String kakaoId);

}
