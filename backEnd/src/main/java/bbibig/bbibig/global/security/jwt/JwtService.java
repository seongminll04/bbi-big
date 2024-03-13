package bbibig.bbibig.global.security.jwt;
import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.security.redis.RedisAccessTokenService;
import bbibig.bbibig.global.security.redis.RedisRefreshTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessTokenName;

    @Value("${jwt.refresh.header}")
    private String refreshTokenName;

    private final RedisAccessTokenService redisAccessTokenService;

    /**
     * JWT의 Subject, Claim으로 email 사용 -> 클레임 name "email"로
     * JWT Header에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

//    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    private final RedisRefreshTokenService redisRefreshTokenService;

    /**
     * AccessToken 생성 메서드
     */
    public String createAccessToken(SocialType socialType, String socialId) {

        Date now = new Date();

        // 유저 존재 유무 확인
        userRepository.findBySocialIdAndSocialType(socialId,socialType)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

        // JWT 토큰 생성
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 만료시간 설정
                .withClaim("id", socialType+"@"+socialId)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, SecretKey로 암호화
    }

    /**
     * RefreshToken 생성 메서드
     */
    public String createRefreshToken() {
        Date now = new Date();

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken Cookie에 실어 보내기
     */
    public void sendAccessCookie(HttpServletResponse httpServletResponse, String accessToken) {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenCookie(httpServletResponse, accessToken);
    }

    /**
     * AcessToken + RefreshToken Cookie에 실어 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse httpServletResponse,
                                          String accessToken, String refreshToken) {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenCookie(httpServletResponse, accessToken);
        setRefreshTokenCookie(httpServletResponse, refreshToken);
    }

    /**
     * AccessToken Cookie 설정
     */
    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        // Access Token을 쿠키로 설정
        Cookie accessTokenCookie = new Cookie(accessTokenName, accessToken);
        accessTokenCookie.setMaxAge(3600); // 1시간 유효한 쿠키로 설정
        accessTokenCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        accessTokenCookie.setHttpOnly(true); // JavaScript로 접근을 막기 위해 HttpOnly 설정
//        accessTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 전송되도록 설정

        response.addCookie(accessTokenCookie);

    }

    /**
     * RefreshToken Cookie 설정
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        // Refresh Token을 쿠키로 설정 (위와 동일한 방식으로 쿠키 생성)
        Cookie refreshTokenCookie = new Cookie(refreshTokenName, refreshToken);
        refreshTokenCookie.setMaxAge(1209600); // 24시간 유효한 쿠키로 설정
        refreshTokenCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        refreshTokenCookie.setHttpOnly(true); // JavaScript로 접근을 막기 위해 HttpOnly 설정
//        refreshTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 전송되도록 설정

        response.addCookie(refreshTokenCookie);

    }

    /**
     * Cookie 삭제
     */
    public void deleteCookie(HttpServletResponse response) {
        // Access Token을 쿠키로 설정
        Cookie accessTokenCookie = new Cookie(accessTokenName,null);
        accessTokenCookie.setMaxAge(0); // 1시간 유효한 쿠키로 설정
        accessTokenCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        accessTokenCookie.setHttpOnly(true); // JavaScript로 접근을 막기 위해 HttpOnly 설정
//        accessTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 전송되도록 설정

        // Refresh Token을 쿠키로 설정 (위와 동일한 방식으로 쿠키 생성)
        Cookie refreshTokenCookie = new Cookie(refreshTokenName, null);
        refreshTokenCookie.setMaxAge(0); // 24시간 유효한 쿠키로 설정
        refreshTokenCookie.setPath("/"); // 모든 경로에서 접근 가능하도록 설정
        refreshTokenCookie.setHttpOnly(true); // JavaScript로 접근을 막기 위해 HttpOnly 설정
//        refreshTokenCookie.setSecure(true); // HTTPS를 사용할 경우에만 전송되도록 설정

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

    }

    /**
     * Cookie에서 RefreshToken 추출
     */
    public Optional<String> extractRefreshToken(HttpServletRequest httpServletRequest) {

        Cookie[] cookies = httpServletRequest.getCookies();
        String RefreshToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(refreshTokenName))
                .findFirst().map(Cookie::getValue).orElse(null);

        return Optional.ofNullable(RefreshToken);
    }

    /**
     * Cookie에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest httpServletRequest) {

        Cookie[] cookies = httpServletRequest.getCookies();
        String AccessToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(accessTokenName))
                .findFirst().map(Cookie::getValue).orElse(null);

        return Optional.ofNullable(AccessToken);
    }

    /**
     * AccessToken으로 Id 추출
     */
    public Optional<String> extractId(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey)) // Token 유효성 검증
                    .build() // JWT verifier 생성
                    .verify(accessToken) // accessToken 검증 -> 유효x : 예외 발생
                    .getClaim("id") // claim에서 id 추출
                    .asString());   // String으로 변환 후 유저 id를 반환
        } catch(Exception e) {
            e.printStackTrace();
            log.error("액세스 토큰이 유효하지 않습니다.");

            return Optional.empty();
        }
    }

    /**
     * RefreshToken Redis 저장 (업데이트)
     */
    public void updateRefreshToken(SocialType socialType,String socialId, String refreshToken) {
        Optional<User> user = userRepository.findBySocialIdAndSocialType(socialId,socialType);

        if(user.isPresent())
            redisRefreshTokenService.setRedisRefreshToken(refreshToken, socialType+"@"+socialId);
        else
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean isTokenValid(String token) {
        try {
            if(redisAccessTokenService.isBlackList(token)) {
                return false;
            }

            JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);

            return true;
        } catch(Exception e) {
            e.printStackTrace();

            return false;
        }
    }
}