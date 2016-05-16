package server.persistentModel.denomination;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
* contains id of denomination, whioh is coocking now
* also user serves and coocking with type of denomination
* it is not good to get data always from Denomination table, as it might have a lot of rows
* such way this entity is written in db I save id of Denomin. and id of Users and when I need I can take them by Id
* */

@Entity
@Table(name = "currentDenomination")
public class CurrentDenomination {

    @Column
    @Id
    private int denomId;

    @Column
    private String userServingLogin;

    @Column
    private String userCoockingLogin;



    public CurrentDenomination() {
    }

    public CurrentDenomination(int denomId, String userServingLogin, String userCoockingLogin) {
        this.denomId = denomId;
        this.userServingLogin = userServingLogin;
        this.userCoockingLogin = userCoockingLogin;
    }

    public int getDenomId() {
        return denomId;
    }

    public void setDenomId(int denomId) {
        this.denomId = denomId;
    }

    public String getUserServingLogin() {
        return userServingLogin;
    }

    public void setUserServingLogin(String userServingLogin) {
        this.userServingLogin = userServingLogin;
    }

    public String getUserCoockingLogin() {
        return userCoockingLogin;
    }

    public void setUserCoockingLogin(String userCoockingLogin) {
        this.userCoockingLogin = userCoockingLogin;
    }

}
