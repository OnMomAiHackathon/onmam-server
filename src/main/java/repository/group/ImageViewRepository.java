package repository.group;

import entity.group.GroupImage;
import entity.group.ImageView;
import entity.user.OnmomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageViewRepository extends JpaRepository<ImageView,Long> {
    List<ImageView> findByGroupImageAndUser(GroupImage groupImage, OnmomUser user);
}
