package bbibig.bbibig.global.oauth2;

import bbibig.bbibig.domain.user.model.SocialType;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/*
 * DefaultOAuth2User를 상속하고, email과 role 필드를 추가로 가짐.
 * Resource Server에서 제공하지 않는 추가 정보들을 가지기 위함.
 * Resourc Server에서 제공하는 정보만 사용해도 된다면 그냥 DefaultOAuth2User 이용
 * */
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private final String socialId;

    private final SocialType socialType;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String socialId, SocialType socialType) {

        super(authorities, attributes, nameAttributeKey);

        this.socialId = socialId;
        this.socialType = socialType;
    }
}