package bbibig.bbibig.domain.server.controller;

import bbibig.bbibig.domain.server.dto.request.CreateServerRequestDto;
import bbibig.bbibig.domain.server.service.GroupServerService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groupserver")
@Slf4j
public class GroupServerController {
    private final GroupServerService groupServerService;

    @ApiOperation(value = "서버 만들기")
    @PostMapping("/createServer")
    public ResponseEntity<?> getMyData(@Valid @RequestBody CreateServerRequestDto createServerRequestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        groupServerService.createServer(createServerRequestDto,userDetails);
        return ResponseEntity.ok().body("생성완료");
    }
}
