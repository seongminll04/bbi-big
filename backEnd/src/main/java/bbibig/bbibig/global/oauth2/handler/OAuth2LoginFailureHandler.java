package bbibig.bbibig.global.oauth2.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 *   소셜 로그인 실패 시 처리 로직
 * */
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException exception) throws IOException {

//        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        httpServletResponse.setCharacterEncoding("UTF-8");
//        httpServletResponse.setContentType("text/plain;charset=UTF-8");
//        httpServletResponse.getWriter().write("소셜 로그인 실패!");
        httpServletResponse.sendRedirect("http://localhost:3000/login");
    }
}