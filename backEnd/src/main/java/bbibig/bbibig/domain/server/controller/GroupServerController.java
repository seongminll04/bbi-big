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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/server")
@Slf4j
public class GroupServerController {
    private final GroupServerService groupServerService;

    @ApiOperation(value = "서버 만들기")
    @PostMapping("/create")
    public ResponseEntity<?> CreateServer(@Valid @RequestPart(value = "serverImg", required = false) MultipartFile serverImg,
                                          @Valid @RequestPart(value = "serverData") CreateServerRequestDto createServerRequestDto,
                                       @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        groupServerService.createServer(serverImg,createServerRequestDto,userDetails);
        return ResponseEntity.ok().body("생성완료");
    }
}
