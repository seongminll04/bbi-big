package bbibig.bbibig.domain.chat.entity;

import bbibig.bbibig.domain.server.entity.Server;
import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.entity.UserServer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "idx")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "server_idx")
    private Server server;

    @ManyToOne
    @JoinColumn(name = "sender_idx")
    private User sender;

    @Column(name = "message")
    private String message;

    @Column(name = "time")
    private LocalDateTime time;

    @Builder
    public Chat (Server server, User sender, String message) {
        this.server=server;
        this.sender=sender;
        this.message=message;
        this.time=LocalDateTime.now();
    }


}
