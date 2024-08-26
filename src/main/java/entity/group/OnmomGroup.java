package entity.group;

import entity.diary.OnmomDiaryEntry;
import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

// (독거노인, 자식으로 구성된) 그룹
@Entity
@Table(name = "onmomGroup")
@Getter
@NoArgsConstructor
public class OnmomGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private LocalDate createdAt;
    private String invitationCode;
    private String groupName;

    // 한 그룹은 N명의 유저로 구성될 수 있다
    @OneToMany(mappedBy = "group")
    private Set<OnmomUser> users;

    // 한 그룹은 여러 다이어리를 가질 수 있다.
    @OneToMany(mappedBy = "group")
    private Set<OnmomDiaryEntry> diaryEntries;

    @Builder
    public OnmomGroup(Long groupId, LocalDate createdAt, String invitationCode, String groupName, Set<OnmomUser> users, Set<OnmomDiaryEntry> diaryEntries) {
        this.groupId = groupId;
        this.createdAt = createdAt;
        this.invitationCode = invitationCode;
        this.groupName = groupName;
        this.users = users;
        this.diaryEntries = diaryEntries;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }

    public void setInvitationCode(String invitationCode){
        this.invitationCode = invitationCode;
    }
}
