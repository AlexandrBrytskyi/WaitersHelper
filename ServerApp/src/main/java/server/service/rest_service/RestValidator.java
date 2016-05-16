package server.service.rest_service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.to.LoginLabel;
import transferFiles.validator.rmiValidator.IValidator;


@RestController
@RequestMapping("/validate")
public class RestValidator implements transferFiles.validator.restValidator.IValidator {


    @Autowired
    @Qualifier("myValidator")
    IValidator validator;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Override
    public User login(@RequestParam("login") String s, @RequestParam("pass") String s1) throws WrongLoginException, WrongPasswordException, AccountBlockedException {
        return validator.login(s, s1);
    }


    @RequestMapping(value = "/sendLabel", method = RequestMethod.POST)
    @Override
    public void sendLabelToValidator(@RequestBody LoginLabel loginLable) {
        validator.sendLabelToValidator(loginLable);
    }


}
