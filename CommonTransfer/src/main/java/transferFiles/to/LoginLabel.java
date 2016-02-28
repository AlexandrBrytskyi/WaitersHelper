package transferFiles.to;

import transferFiles.model.user.User;

import java.io.Serializable;


public class LoginLabel implements Serializable {

    private User user;
    private String desc = "";


    public LoginLabel() {
    }

    public LoginLabel(User user, String desc) {
        this.desc = desc;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginLabel label = (LoginLabel) o;

        if (!user.equals(label.user)) return false;
        return desc.equals(label.desc);

    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + desc.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "desc='" + desc + '\'' +
                '}';
    }
}
