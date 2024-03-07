package bbibig.bbibig.domain.user.repository;

import bbibig.bbibig.domain.user.entity.Friendship;
import bbibig.bbibig.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    List<Friendship> findAllByUser(User user);

    Optional<Friendship> findByFriendIdxAndUser(Long userIdx, User user);
}
