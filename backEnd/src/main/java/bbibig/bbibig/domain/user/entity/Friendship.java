package bbibig.bbibig.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "friendship_idx")
    private Long id;

    @Column(name = "friendship_friend_idx", unique = true)
    private Long friendIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @Builder
    public Friendship(Long friendIdx, User user){
        this.friendIdx = friendIdx;
        this.changeUser(user);
    }

    public void changeUser(User user){
        this.user = user;
        user.getFriendships().add(this);
    }

}
