package client.view.barmen;


import transferFiles.model.user.User;
import transferFiles.service.rmiService.IBarmenService;

import java.awt.*;

public abstract class BarmenUIDecorator extends BarmenUI {

    public BarmenUIDecorator(IBarmenService service, User logged) throws HeadlessException {
        super(service, logged);
        accountPane.removeChangeListener(accountPane.getChangeListeners()[0]);
    }

}
