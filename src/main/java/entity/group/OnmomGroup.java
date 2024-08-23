package entity.group;

import entity.diary.OnmomDiaryEntry;
import entity.user.OnmomUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

// (독거노인, 자식으로 구성된) 그룹
@Entity
@Table(name = "onmomGroup")
@Getter
@Setter
public class OnmomGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;
    private LocalDate createdAt;
    private String invitationCode;

    // 한 그룹은 N명의 유저로 구성될 수 있다
    @OneToMany(mappedBy = "group")
    private Set<OnmomUser> users;

    // 한 그룹은 여러 다이어리를 가질 수 있다.
    @OneToMany(mappedBy = "group")
    private Set<OnmomDiaryEntry> diaryEntries;

}
