package entity.group;

import entity.diary.OnmomDiaryEntry;
import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "onmomGroup")
@Getter
@NoArgsConstructor
public class OnmomGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private String groupName;
    private String invitationCode;
    private String groupImageUrl;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "group")
    private Set<OnmomUser> users;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<OnmomDiaryEntry> diaryEntries;

    @Builder
    public OnmomGroup(Long groupId, LocalDate createdAt, String invitationCode, String groupName, String groupImageUrl, Set<OnmomUser> users, Set<OnmomDiaryEntry> diaryEntries) {
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.invitationCode = invitationCode;
        this.groupName = groupName;
        this.groupImageUrl = groupImageUrl;
        this.users = users;
        this.diaryEntries = diaryEntries;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public void setInvitationCode(String invitationCode){
        this.invitationCode = invitationCode;
    }

    public void setGroupImageUrl(String groupImageUrl){
        this.groupImageUrl = groupImageUrl;
    }
}
