package bbibig.bbibig.domain.user.repository;

import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.model.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findBySocialIdAndSocialType(String socialId, SocialType socialType);

    Optional<User> findByNickname(String nickname);

}
