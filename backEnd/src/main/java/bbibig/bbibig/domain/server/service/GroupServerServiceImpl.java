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
import bbibig.bbibig.global.s3service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GroupServerServiceImpl implements GroupServerService{

    private final UserRepository userRepository;

    private final GroupServerRepository groupServerRepository;

    private final S3Service s3Service;
    /**
     * 서버 만들기
     */
    @Override
    @Transactional
    public void createServer(MultipartFile serverImg, CreateServerRequestDto serverData, UserDetails userDetails) throws IOException {
        // 내 계정정보 불러오기
        User user = userRepository.findBySocialIdAndSocialType(userDetails.getUsername(), SocialType.valueOf(userDetails.getPassword()))
                .orElseThrow(() -> new EmptyResultDataAccessException("해당 유저는 존재하지 않습니다.", 1));

        GroupServer groupServer=GroupServer.builder()
                .serverName(serverData.getServername())
                .joinMethod(serverData.getJoinMethod())
                .searchOpen(serverData.getSearchOpen())
                .admin(user)
                .build();

        GroupServer createdServer = groupServerRepository.save(groupServer);

        if (serverImg != null) {
            // 저장할 serverImg 파일을 넘겨서 업로드 -> 랜덤 난수로 파일명을 생성하여 파일 url 넘겨줌
            String serverImgPath = s3Service.serverImgUpload(serverImg);

            createdServer.updateServerImg(serverImgPath);
        }
    }

}
