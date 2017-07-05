package Server;

/**
 * Created by ina on 2017-06-01.
 * TODO
 */
public class ServerAuthorizer {

    String userName;
    String userPassword;

    public ServerAuthorizer() {

    }

    public void setUserInfo(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public boolean authorize(){
        //TODO
        return true;
    }
}
