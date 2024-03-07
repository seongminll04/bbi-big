package bbibig.bbibig.global.oauth2;

import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.global.oauth2.userinfo.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/*
 * 각 소셜 별로 제공하는 데이터가 다름.
 * 소셜 별로 데이터를 받는 데이터를 분기 처리하는 DTO
 * */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthAttributes {

    // OAuth2 로그인 진행 시 Key가 되는 필드 값, PK와 같은 의미
    private String nameAttributeKey;

    // 소셜 타입 별 로그인 유저 정보 (닉네임, 이메일, 프로필 사진 등)
    private OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    /*
     * SocialType에 맞는 메서드 호출 => OAuthAttributes 객체 반환
     * */
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if(socialType == SocialType.NAVER)
            return ofNaver(userNameAttributeName, attributes);
        else if(socialType == SocialType.KAKAO)
            return ofKakao(userNameAttributeName, attributes);
        else if(socialType == SocialType.GOOGLE)
            return ofGoogle(userNameAttributeName, attributes);
        else
            return null;
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }
}

/*
 * User 엔티티 객체 생성
 * */
//public User toEntity(String socialType, OAuth2UserInfo oAuth2UserInfo) {
//    return User.builder()
//            .socialType(socialType)
//            .socialId(oAuth2UserInfo.getId())
//            .email(UUID.randomUUID() + "@socialUser.com")
////                .gender(oAuth2UserInfo.getGender())
////                .profileImg(oAuth2UserInfo.getProfileImg())
//            .profileImg("https://gogosing.s3.ap-northeast-2.amazonaws.com/DefaultProfile.png")
//            .role(Role.GUEST)
//            .build();