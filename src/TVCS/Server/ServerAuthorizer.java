package TVCS.Server;

/**
 * Created by ina on 2017-06-01.
 * TODO
 */
public class ServerAuthorizer {

    String userName;
    String userPassword;

    public ServerAuthorizer() {

    }

    public void setUserName(String userName) {
        this.userName = userName;
        userPassword = "1111";
    }

    public boolean Authorize(String password){
        if(password.equals(userPassword)) {
            return true;
        }
        return false;
    }

}
