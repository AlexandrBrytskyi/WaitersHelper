package client.view.barmen;


import server.model.user.User;
import server.service.IBarmenService;

import java.awt.*;

public abstract class BarmenUIDecorator extends BarmenUI {

    public BarmenUIDecorator(IBarmenService service, User logged) throws HeadlessException {
        super(service, logged);
        accountPane.removeChangeListener(accountPane.getChangeListeners()[0]);
    }

}
