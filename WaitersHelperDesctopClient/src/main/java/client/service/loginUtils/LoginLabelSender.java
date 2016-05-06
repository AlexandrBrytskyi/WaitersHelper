package client.service.loginUtils;


import client.validator.IValidator;
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
                validator.sendLabelToValidator(loginLabel);
            } catch (Throwable e) {
                LOGGER.error(e);
                Thread.currentThread().start();
            }
        }
    }
}
