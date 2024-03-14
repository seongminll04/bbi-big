package bbibig.bbibig.domain.server.service;

import bbibig.bbibig.domain.server.dto.request.CreateServerRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface GroupServerService {
    void createServer(CreateServerRequestDto createServerRequestDto, UserDetails userDetails);
}
