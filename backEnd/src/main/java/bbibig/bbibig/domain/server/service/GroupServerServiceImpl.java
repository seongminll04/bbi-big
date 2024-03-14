package bbibig.bbibig.domain.server.service;

import bbibig.bbibig.domain.server.dto.request.CreateServerRequestDto;
import bbibig.bbibig.domain.server.entity.GroupServer;
import bbibig.bbibig.domain.server.repository.GroupServerRepository;
import bbibig.bbibig.domain.user.dto.response.GetMyDataResponseDto;
import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.entity.UserServer;
import bbibig.bbibig.domain.user.model.SocialType;
import bbibig.bbibig.domain.user.repository.UserRepository;
import bbibig.bbibig.domain.user.repository.UserServerRepository;
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
public class GroupServerServiceImpl implements GroupServerService{

    private final UserRepository userRepository;

    private final GroupServerRepository groupServerRepository;
    private final UserServerRepository userServerRepository;
    /**
     * 서버 만들기
     */
    @Override
    @Transactional
    public void createServer(CreateServerRequestDto createServerRequestDto, UserDetails userDetails) {
        // 내 계정정보 불러오기
        User user = userRepository.findBySocialIdAndSocialType(userDetails.getUsername(), SocialType.valueOf(userDetails.getPassword()))
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        GroupServer groupServer=GroupServer.builder()
                .serverName(createServerRequestDto.getServerName())
                .serverImg(createServerRequestDto.getServerImg())
                .serverRegist(createServerRequestDto.getServerRegist())
                .serverSearch(createServerRequestDto.getServerSearch())
                .admin(user)
                .build();
        GroupServer createdServer = groupServerRepository.save(groupServer);

        UserServer userServer = UserServer.builder()
                .user(user)
                .server(createdServer)
                .build();
        userServerRepository.save(userServer);
    }
}
