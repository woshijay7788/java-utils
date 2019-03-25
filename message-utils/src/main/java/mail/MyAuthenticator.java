package mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Description: []
 * </p>
 *
 * @author chibei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyAuthenticator extends Authenticator {

    private String username;
    private String password;

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
