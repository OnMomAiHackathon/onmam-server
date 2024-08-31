package repository.group;

import entity.group.OnmomGroup;
import entity.group.UserNickname;
import entity.user.OnmomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNicknameRepository extends JpaRepository<UserNickname, Long> {

    Optional<UserNickname> findByUserAndGroupAndTargetUser(OnmomUser user, OnmomGroup group, OnmomUser targetUser);

}
