package entity.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_image")
@Getter
@NoArgsConstructor
public class GroupImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "groupId")
    private OnmomGroup group;

    @ManyToOne
    @JoinColumn(name = "userId")
    private OnmomUser user;

    @Builder
    public GroupImage(String imageUrl, LocalDateTime uploadedAt, OnmomGroup group, OnmomUser user) {
        this.imageUrl = imageUrl;
        this.uploadedAt = uploadedAt;
        this.group = group;
        this.user = user;
    }

}
