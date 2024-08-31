package entity.group;

import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name="image_view")
@Getter
@NoArgsConstructor
public class ImageView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "imageId")
    private GroupImage groupImage;

    @ManyToOne
    @JoinColumn(name = "userId")
    private OnmomUser user;

    private LocalDateTime viewedAt;

    @Builder
    public ImageView(GroupImage groupImage, OnmomUser user, LocalDateTime viewedAt) {
        this.groupImage = groupImage;
        this.user = user;
        this.viewedAt = viewedAt;
    }

}
