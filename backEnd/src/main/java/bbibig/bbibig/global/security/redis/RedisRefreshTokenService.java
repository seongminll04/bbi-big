package bbibig.bbibig.global.security.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final RedisTemplate redisTemplate;

    /**
     * Key-Value 설정
     */
    @Transactional
    public void setRedisRefreshToken(String refreshToken, String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        // 추후 access token으로 유효한지 판단해보기
        valueOperations.set("[RefreshToken] : " + refreshToken, id, Duration.ofMinutes(20160));
        valueOperations.set(id, "[RefreshToken] : " + refreshToken, Duration.ofMinutes(20160));
    }

    /**
     * Key-Value 삭제
     */
    @Transactional
    public void deleteRefreshToken(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String refreshToken = valueOperations.get(id);

        if(refreshToken == null) {
            System.out.println("해당 요소(Email, RefreshToken)가 Redis에 존재하지 않아서 삭제할 수 없습니다.");
            return;
        }

        Boolean deleteRefreshTokenCheck = redisTemplate.delete(refreshToken);
        Boolean deleteEmailCheck = redisTemplate.delete(id);

        if (deleteRefreshTokenCheck && deleteEmailCheck) {
            System.out.println("Keys deleted");
        } else {
            System.out.println("Keys no deleted");
        }
    }

    /**
     * Key로 Value 조회
     */
    public String getRedisId(String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return valueOperations.get("[RefreshToken] : " + refreshToken);
    }

    public String getRedisRefreshToken(String id) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String RefreshToken = valueOperations.get(id);

        return RefreshToken.replace("[RefreshToken] : ", "");
    }

}