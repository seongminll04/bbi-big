package bbibig.bbibig.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeNicknameRequestDto {
    @NotBlank(message = "nickname은 빈값이 올 수 없습니다")
    private String nickname;

    @Builder
    public ChangeNicknameRequestDto(String nickname){
        this.nickname = nickname;
    }
}
