package server.service.password_utils;


public class Password {

    private String pass;

    public Password(String pass) {
        this.pass = pass;
    }

    public boolean compare(int anotherPassHash) {
        return (this.hashCode() == anotherPassHash);
    }

    @Override
    public int hashCode() {
        return pass != null ? pass.hashCode() : 0;
    }
}
