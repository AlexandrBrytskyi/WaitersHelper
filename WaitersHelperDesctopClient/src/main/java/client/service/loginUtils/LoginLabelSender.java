package client.service.loginUtils;


import client.validator.IValidator;
import transferFiles.to.LoginLabel;


public class LoginLabelSender implements Runnable {


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
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
    }
}
