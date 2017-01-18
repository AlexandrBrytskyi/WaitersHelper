package server.persistentModel.user;

import transferFiles.model.user.UserType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "user")
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

    public User(String name, String login, int pass, UserType type, boolean locked) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        this.type = type;
        this.locked = locked;
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

    public void setPass(int pass) {
        this.pass = pass;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        return type == user.type;

    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }

    public transferFiles.model.user.User toTransferUser() {
        return new transferFiles.model.user.User(this.getName(), this.getLogin(), this.pass, this.getType(), this.isLocked());
    }

    public static User toPersistentUser(transferFiles.model.user.User user) {
        return new User(user.getName(),
                user.getLogin(),
                user.getPass(),
                user.getType(),
                user.isLocked());
    }

    public static List<transferFiles.model.user.User> getListTOUsers(List<User> persistentUsers) {
        List<transferFiles.model.user.User> usersTO = new LinkedList<>();
        for (User persistentUser : persistentUsers) {
            usersTO.add(persistentUser.toTransferUser());
        }
        return usersTO;
    }
}
