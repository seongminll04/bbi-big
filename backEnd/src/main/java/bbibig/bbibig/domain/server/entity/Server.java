package bbibig.bbibig.domain.server.entity;


import bbibig.bbibig.domain.chat.entity.Chat;
import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.entity.UserServer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "server_type")
@Table(name = "servers")
public abstract class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    protected Long id;

    // 서버와 유저 간의 One-to-Many 관계 설정
    @OneToMany(mappedBy = "server")
    private Set<UserServer> userServers = new HashSet<>();

    @OneToMany(mappedBy = "server")
    private Set<Chat> sendChats = new HashSet<>();

    /**
     * 서버에 유저 추가
     */
    public void addUser(User user) {
        UserServer userServer = new UserServer(user, this);
        userServers.add(userServer);
        user.getUserServers().add(userServer);
    }


}
