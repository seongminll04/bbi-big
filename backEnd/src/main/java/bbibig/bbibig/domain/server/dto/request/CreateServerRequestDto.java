package bbibig.bbibig.domain.server.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateServerRequestDto {
    private String serverName;
    private String serverImg;
    private Boolean serverRegist;
    private Boolean serverSearch;
}
