package bbibig.bbibig.domain.user.entity;

import bbibig.bbibig.domain.chat.entity.Chat;
import bbibig.bbibig.domain.server.entity.GroupServer;
import bbibig.bbibig.domain.user.model.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    protected Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    protected SocialType socialType;

    @Column(name = "social_id")
    protected String socialId;

    @Column(name = "user_nickname", unique = true)
    protected String nickname;

    @Column(name = "user_img_url")
    protected String imgUrl;

    @Column(name = "user_alarm")
    private Boolean alarm;

    // 조인 엔티티와의 One-to-Many 관계 설정
    @OneToMany(mappedBy = "user")
    private Set<UserServer> userServers = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<Chat> sendChats = new HashSet<>();

    @OneToMany(mappedBy = "admin")
    private Set<GroupServer> admin = new HashSet<>();

    @Builder
    public User(SocialType socialType, String socialId, String imgUrl){
        this.socialType = socialType;
        this.socialId = socialId;
//        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.alarm = false;
    }

    /**
     * 닉네임 변경
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 유저 알람 설정 on/off
     */
    public void alarmOnOff(Boolean alarmSet) {
        this.alarm = !alarmSet;
    }

    /**
     * 유저 프로필 이미지 변경
     */
    public void updateProfileImg(String imgPath) {
        this.imgUrl=imgPath;
    }

}
