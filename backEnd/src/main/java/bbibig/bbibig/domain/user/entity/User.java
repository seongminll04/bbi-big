package bbibig.bbibig.domain.user.entity;

import bbibig.bbibig.domain.user.model.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
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

    @OneToMany(mappedBy = "user")
    private List<Friendship> friendships = new ArrayList<>();

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

    /**
     * 비밀번호 암호화
     */
//    public void passwordEncode(PasswordEncoder passwordEncoder) {
//        this.password = passwordEncoder.encode(this.password);
//    }
}
