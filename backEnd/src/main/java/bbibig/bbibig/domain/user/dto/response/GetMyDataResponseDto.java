package bbibig.bbibig.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetMyDataResponseDto {
    private String nickname;
    private String profileImg;
    private Long tagNum;

    @Builder
    public GetMyDataResponseDto(String nickname, String profileImg,Long tagNum) {
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.tagNum = tagNum;
    }
}
