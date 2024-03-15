package bbibig.bbibig.domain.server.service;

import bbibig.bbibig.domain.server.dto.request.CreateServerRequestDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface GroupServerService {
    void createServer(MultipartFile serverImg, CreateServerRequestDto createServerRequestDto, UserDetails userDetails) throws IOException;
}
