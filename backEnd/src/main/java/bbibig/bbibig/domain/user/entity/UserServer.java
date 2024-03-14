package bbibig.bbibig.domain.user.entity;

import bbibig.bbibig.domain.chat.entity.Chat;
import bbibig.bbibig.domain.server.entity.Server;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_server")
public class UserServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    protected Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Server server;

    @Builder
    public UserServer(User user, Server server) {
        this.user = user;
        this.server = server;
    }
}
