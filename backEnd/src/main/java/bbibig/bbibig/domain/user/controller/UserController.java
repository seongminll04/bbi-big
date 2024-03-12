package bbibig.bbibig.domain.user.controller;

import bbibig.bbibig.domain.user.dto.request.ChangeNicknameRequestDto;
import bbibig.bbibig.domain.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;
    @ApiOperation(value = "내 정보 조회")
    @GetMapping("/getMyData")
    public ResponseEntity<?> getMyData(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(userService.getMyData(userDetails));
    }

    @ApiOperation(value = "닉네임 수정")
    @PostMapping("/changeNickname")
    public ResponseEntity<?> changeNickname(@RequestBody ChangeNicknameRequestDto changeNicknameRequestDto,
                                         @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        return ResponseEntity.ok().body(userService.changeNickname(changeNicknameRequestDto.getNickname(), userDetails));
    }

    @ApiOperation(value = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse httpServletResponse) {
        userService.logout(httpServletResponse);
        return ResponseEntity.ok().body("");
    }
}
