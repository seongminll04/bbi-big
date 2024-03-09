package bbibig.bbibig.global.login.handler;

import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.security.jwt.JwtService;
import bbibig.bbibig.global.security.redis.RedisRefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * JWT를 활용한 일반 로그인 성공 처리
 */
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final RedisRefreshTokenService redisRefreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {

        // 인증 정보에서 username(id) 추출
        String id = extractUsername(authentication);
        String[] parts = id.split("@");

        SocialType socialType = SocialType.valueOf(parts[0]);
        String socialId = parts[1];

        // AccessToken & RefreshToken 발급
        String accessToken = jwtService.createAccessToken(socialType,socialId);
        String refreshToken = jwtService.createRefreshToken();

        httpServletResponse.addHeader(jwtService.getAccessHeader(), accessToken);
        httpServletResponse.addHeader(jwtService.getRefreshHeader(), refreshToken);
//        httpServletResponse.addHeader("new_basic_user_id", id);
//
//        Cookie emailCookie = new Cookie("new_basic_user_id", id);
//        emailCookie.setMaxAge(600);
//        emailCookie.setPath("/");
//        httpServletResponse.addCookie(emailCookie);

        // response header에 AccessToken, RefreshToken 실어서 보내기
        jwtService.sendAccessAndRefreshToken(httpServletResponse, accessToken, refreshToken);

        User user = userRepository.findBySocialIdAndSocialType(socialId,socialType)
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        if(user != null) {
            // Redis에 RefreshToken 저장
            redisRefreshTokenService.setRedisRefreshToken(refreshToken, id);
        }
        else
            throw new NullPointerException("해당 유저가 존재하지 않습니다.");
    }

    /**
     * Authentication(인증 정보)로부터 username(id) 추출하기
     */
    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getUsername();
    }
}