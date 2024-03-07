package bbibig.bbibig.global.security.jwt;
import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.security.redis.RedisAccessTokenService;
import bbibig.bbibig.global.security.redis.RedisRefreshTokenService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private final RedisAccessTokenService redisAccessTokenService;

    /**
     * JWT의 Subject, Claim으로 email 사용 -> 클레임 name "email"로
     * JWT Header에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";

    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    private final RedisRefreshTokenService redisRefreshTokenService;

    /**
     * AccessToken 생성 메서드
     */
    public String createAccessToken(SocialType socialType, String socialId) {

        Date now = new Date();

        // 유저 존재 유무 확인
        User user = userRepository.findBySocialIdAndSocialType(socialId,socialType)
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
     * AccessToken Header에 실어 보내기
     */
    public void sendAccessToken(HttpServletResponse httpServletResponse, String accessToken) {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(httpServletResponse, accessToken);
    }

    /**
     * AcessToken + RefreshToken Header에 실어 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse httpServletResponse,
                                          String accessToken, String refreshToken) {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(httpServletResponse, accessToken);
        setRefreshTokenHeader(httpServletResponse, refreshToken);
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * Header에서 RefreshToken 추출
     * Bearer를 제외하고 순수 토큰만 가져오기 위해서 BEARER 제거
     */
    public Optional<String> extractRefreshToken(HttpServletRequest httpServletRequest) {

        return Optional.ofNullable(httpServletRequest.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * Header에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest httpServletRequest) {

        return Optional.ofNullable(httpServletRequest.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }


    /**
     * AccessToken에서 Email 추출
     */
    public Optional<String> extractEmail(String accessToken) {
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