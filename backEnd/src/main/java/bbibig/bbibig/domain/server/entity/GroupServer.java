package bbibig.bbibig.domain.server.entity;


import bbibig.bbibig.domain.user.entity.User;
import bbibig.bbibig.domain.user.entity.UserServer;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("group")
public class GroupServer extends Server {

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "server_Img")
    private String serverImg;

    @Column(name = "server_regist")
    private Boolean serverRegist;

    @Column(name = "server_search")
    private Boolean serverSearch;

    @ManyToOne
    private User admin;

    @Builder
    public GroupServer (String serverName, String serverImg, Boolean serverRegist, Boolean serverSearch, User admin) {
        this.serverName=serverName;
        this.serverImg=serverImg;
        this.serverRegist=serverRegist;
        this.serverSearch=serverSearch;
        this.admin=admin;
        this.addUser(admin);
    }


}
