package server.model.user;

import server.exceptions.WrongPasswordException;
import server.service.password_utils.Password;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class User implements Serializable {

    @Column
    private String name;

    @Column
    @Id
    private String login = "standardlogin";

    @Column
    private int pass;

    @Column
    @Enumerated
    private UserType type;

    @Column
    private boolean locked = false;


    public User() {

    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(String pass) throws WrongPasswordException {
        this.pass = new Password(pass).hashCode();
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
