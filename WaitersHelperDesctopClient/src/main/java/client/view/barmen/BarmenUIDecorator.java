package client.view.barmen;


import client.service.IBarmenService;
import transferFiles.model.user.User;

import java.awt.*;

public abstract class BarmenUIDecorator extends BarmenUI {

    public BarmenUIDecorator(IBarmenService service, User logged) throws HeadlessException {
        super(service, logged);
        accountPane.removeChangeListener(accountPane.getChangeListeners()[0]);
    }

}
