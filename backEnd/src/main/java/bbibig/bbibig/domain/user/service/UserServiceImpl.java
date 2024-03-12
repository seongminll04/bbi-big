package bbibig.bbibig.domain.user.service;

import bbibig.bbibig.domain.user.dto.response.GetMyDataResponseDto;
import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.global.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    /**
     * 내 정보 조회
     */
    @Override
    public GetMyDataResponseDto getMyData(UserDetails userDetails) {
        // 내 계정정보 불러오기
        User user = userRepository.findBySocialIdAndSocialType(userDetails.getUsername(), SocialType.valueOf(userDetails.getPassword()))
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        return GetMyDataResponseDto.builder()
                .nickname(user.getNickname())
                .profileImg(user.getImgUrl())
                .build();
    }

    /**
     * 닉네임 등록 (첫 로그인 시)
     */
    @Transactional
    @Override
    public String changeNickname(String nickname, UserDetails userDetails) throws Exception {
        // 내 계정정보 불러오기
        User user = userRepository.findBySocialIdAndSocialType(userDetails.getUsername(), SocialType.valueOf(userDetails.getPassword()))
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        // 닉네임 등록
        user.updateNickname(nickname);
        // 그리고 저장
        userRepository.save(user);

        return "닉네임 등록 완료";
    }
    /**
     * 로그아웃
     */

    @Override
    public void logout(HttpServletResponse httpServletResponse) {
        Cookie access = new Cookie(jwtService.getAccessTokenName(),null);
        access.setMaxAge(0);
        access.setPath("/");
        Cookie refresh = new Cookie(jwtService.getRefreshTokenName(),null);
        refresh.setMaxAge(0);
        refresh.setPath("/");
        httpServletResponse.addCookie(access);
        httpServletResponse.addCookie(refresh);
    }
}
