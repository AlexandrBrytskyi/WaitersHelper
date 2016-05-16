package server.service.rest_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import transferFiles.exceptions.UserAccessException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.ChangeNamePassRequest;
import transferFiles.service.restService.restRequstObjects.CockCancelDenomRequest;
import transferFiles.service.rmiService.ICookService;
import transferFiles.to.LoginLabel;

import java.util.List;

@RestController("restCockService")
@RequestMapping(value = "/service/cook")
public class RestCockService implements transferFiles.service.restService.ICookService {

    @Autowired
    @Qualifier("cookService")
   private ICookService cookService;

    @Override
    @RequestMapping(value = "cancelDenomination", method = RequestMethod.POST)
    public Denomination cancelDenomination(@RequestBody CockCancelDenomRequest request) throws UserAccessException {
        return cookService.cancelDenomination(request.getDenomination(), request.getLogined());
    }

    @Override
    @RequestMapping(value = "setDenomStateReady", method = RequestMethod.POST)
    public Denomination setDenomStateReady(@RequestBody Denomination denomination) throws UserAccessException {
        return cookService.setDenomStateReady(denomination);
    }

    @Override
    @RequestMapping(value = "/sentUIobjectToValidator", method = RequestMethod.POST)
    public void sentUIobjectToValidator(@RequestBody LoginLabel loginLabel) {
        cookService.sentUIobjectToValidator(loginLabel);
    }

    @Override
    @RequestMapping(value = "/getWorkingDenoms", method = RequestMethod.POST)
    public List<Denomination> getWorkingDenoms(@RequestBody User user) {
        return cookService.getWorkingDenoms(user);
    }

    @Override
    @RequestMapping(value = "/getNewDenominations", method = RequestMethod.POST)
    public List<Denomination> getNewDenominations(@RequestBody User user) {
        return cookService.getNewDenominations(user);
    }

    @Override
    @RequestMapping(value = "/getMessages", method = RequestMethod.POST)
    public List<Denomination> getMessages(@RequestBody User user) {
        return cookService.getMessages(user);
    }


    @Override
    @RequestMapping(value = "/changeName", method = RequestMethod.POST)
    public User changeName(@RequestBody ChangeNamePassRequest request) {
        return cookService.changeName(request.getUser(), request.getValStr());
    }

    @Override
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public User changePassword(ChangeNamePassRequest request) throws WrongPasswordException {
        return cookService.changePassword(request.getUser(), request.getValStr());
    }
}
