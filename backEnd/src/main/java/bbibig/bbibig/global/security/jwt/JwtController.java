package bbibig.bbibig.global.security.jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh")
@Slf4j
public class JwtController {
    @GetMapping
    @PostMapping
    public ResponseEntity<?> getRetoken() {
        return org.springframework.http.ResponseEntity.ok().body("");
    }
}
