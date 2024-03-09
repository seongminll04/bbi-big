package bbibig.bbibig.global.oauth2.service;

import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.oauth2.OAuthAttributes;
import bbibig.bbibig.global.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // loadUser : 소셜 로그인 API의 사용자 정보 제공 URI로 요청
        // => 사용자 정보를 얻은 후 객체 반환
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // userRequest에서 registrationId 추출 후 SocialType 저장
        // socialType 'naver', 'kakao', 'google' 값이 들어올 수 있음
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        // OAuth2 로그인 시 PK
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 해당 소셜에 따라서 매칭
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        // 소셜 정보로 유저정보 조회
        User loadUser = getUser(Objects.requireNonNull(oAuthAttributes), socialType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(loadUser.getSocialId())),
                attributes,
                oAuthAttributes.getNameAttributeKey(),
                loadUser.getSocialId(),
                loadUser.getSocialType()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if(registrationId.equals("naver"))
            return SocialType.NAVER;

        if(registrationId.equals("kakao"))
            return SocialType.KAKAO;

        if(registrationId.equals("google"))
            return SocialType.GOOGLE;

        return null;
    }
    private User getUser(OAuthAttributes oAuthAttributes, SocialType socialType) {
        User user = userRepository.findBySocialIdAndSocialType(
                        oAuthAttributes.getOAuth2UserInfo().getId(),
                        socialType)
                .orElse(null);

        // 유저정보가 없으면 회원가입 해주고 정보를 return
        if(user == null)
            return signUp(oAuthAttributes, socialType);

        return user;
    }

    private User signUp(OAuthAttributes oAuthAttributes, SocialType socialType) {
        User user = User.builder()
                .socialId(oAuthAttributes.getOAuth2UserInfo().getId())
                .socialType(socialType)
                .imgUrl(oAuthAttributes.getOAuth2UserInfo().getProfileImg())
                .build();

        return userRepository.save(user);
    }
}
