package bbibig.bbibig.global.oauth2.handler;

import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.oauth2.CustomOAuth2User;
import bbibig.bbibig.global.security.jwt.JwtService;
import bbibig.bbibig.global.security.redis.RedisRefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * 소셜 로그인 성공 시 처리 로직
 * */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String targetUrl = loginSuccess(httpServletResponse, oAuth2User);
            httpServletResponse.sendRedirect(targetUrl);

        } catch(Exception e) {
            e.printStackTrace();

            throw e;
        }
    }

    private String loginSuccess(HttpServletResponse httpServletResponse, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getSocialType(),oAuth2User.getSocialId());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(httpServletResponse, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getSocialType(),oAuth2User.getSocialId(), refreshToken);


        // 로그인 후 이동할 주소
        return UriComponentsBuilder.fromUriString("http://localhost:3000/")
                .build()
                .toUriString();
    }

//    public Map<String, String> socialLoginSuccessAndSendTokenToFront() {
//        Map<String, String> map = new HashMap<>();
//
//        map.put("Authorization", access);
//        map.put("Authorization-Refresh", refresh);
//
//        return map;
//    }
}