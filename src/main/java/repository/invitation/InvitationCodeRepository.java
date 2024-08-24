package repository.invitation;

import entity.invitation.OnmomInvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationCodeRepository extends JpaRepository<OnmomInvitationCode,Long> {
}
