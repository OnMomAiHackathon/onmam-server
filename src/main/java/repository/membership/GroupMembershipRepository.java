package repository.membership;

import entity.membership.OnmomGroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMembershipRepository extends JpaRepository<OnmomGroupMembership,Long> {
}
