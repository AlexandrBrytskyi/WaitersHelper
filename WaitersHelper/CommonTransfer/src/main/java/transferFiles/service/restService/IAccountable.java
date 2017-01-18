package transferFiles.service.restService;


import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.ChangeNamePassRequest;

public interface IAccountable {

    User changeName(ChangeNamePassRequest request);

    User changePassword(ChangeNamePassRequest request) throws WrongPasswordException;

}
