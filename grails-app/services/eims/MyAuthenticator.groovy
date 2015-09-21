package eims
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Created by IntelliJ IDEA.
 * User: xie
 * Date: 13-7-16
 * Time: 下午5:08
 * To change this template use File | Settings | File Templates.
 */
class MyAuthenticator  extends Authenticator {

    String userName = null;
    String password = null;

    public MyAuthenticator() {
    }

    public MyAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
