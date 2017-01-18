package client.service.loginUtils;


import org.springframework.remoting.RemoteLookupFailureException;
import transferFiles.validator.rmiValidator.IValidator;
import org.apache.log4j.Logger;
import transferFiles.to.LoginLabel;


public class LoginLabelSender implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(LoginLabelSender.class);
    private IValidator validator;
    private LoginLabel loginLabel;

    public LoginLabelSender(IValidator validator, LoginLabel loginLabel) {
        this.validator = validator;
        this.loginLabel = loginLabel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(30000);
                try {
                    validator.sendLabelToValidator(loginLabel);
                } catch (RemoteLookupFailureException e) {
                    Thread.currentThread().sleep(20000);
                }
            } catch (Throwable e) {
                LOGGER.error(e);
                Thread.currentThread().start();
            }
        }
    }
}
