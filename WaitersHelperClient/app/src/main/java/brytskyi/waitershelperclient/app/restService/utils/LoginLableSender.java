package brytskyi.waitershelperclient.app.restService.utils;


import transferFiles.to.LoginLabel;
import transferFiles.validator.restValidator.IValidator;

public class LoginLableSender implements Runnable {

    private LoginLabel loginLabel;
    private IValidator validator;

    public LoginLableSender(LoginLabel loginLabel, IValidator validator) {
        this.loginLabel = loginLabel;
        this.validator = validator;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.currentThread().sleep(30000);
                validator.sendLabelToValidator(loginLabel);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
