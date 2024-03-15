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

    @Column(name = "serverImg")
    private String serverImg;

    @Column(name = "joinMethod")
    private Boolean joinMethod;

    @Column(name = "searchOpen")
    private Boolean searchOpen;

    @ManyToOne
    private User admin;

    @Builder
    public GroupServer (String serverName, Boolean joinMethod, Boolean searchOpen, User admin) {
        this.serverName=serverName;
        this.joinMethod=joinMethod;
        this.searchOpen=searchOpen;
        this.admin=admin;
        this.addUser(admin);
    }


    /**
     * 서버 이미지 변경
     */
    public void updateServerImg(String serverImg) {
        this.serverImg = serverImg;
    }


}
