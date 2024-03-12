package bbibig.bbibig.domain.user.service;


import bbibig.bbibig.domain.user.dto.response.GetMyDataResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    String changeNickname(String nickname, UserDetails userDetails) throws Exception;

    GetMyDataResponseDto getMyData(UserDetails userDetails);

    void logout(HttpServletResponse httpServletResponse);
}
