package bbibig.bbibig.domain.server.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class CreateServerRequestDto {
    private String servername;
    private Boolean joinMethod;
    private Boolean searchOpen;
}
