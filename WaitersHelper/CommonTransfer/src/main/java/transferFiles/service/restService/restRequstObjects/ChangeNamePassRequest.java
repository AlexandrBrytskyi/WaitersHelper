package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.user.User;

/**
 * User: huyti
 * Date: 16.05.2016
 */
public class ChangeNamePassRequest {

    private User user;
    private String valStr;

    public ChangeNamePassRequest() {
    }

    public ChangeNamePassRequest(User user, String valStr) {
        this.user = user;
        this.valStr = valStr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValStr() {
        return valStr;
    }

    public void setValStr(String valStr) {
        this.valStr = valStr;
    }
}
