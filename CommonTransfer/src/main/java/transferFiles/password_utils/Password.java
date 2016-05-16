package transferFiles.password_utils;


import transferFiles.exceptions.WrongPasswordException;

import java.io.Serializable;

public class Password implements Serializable {

    private String pass;

    public Password(String pass) throws WrongPasswordException {
        if (pass.length() < 5) throw new WrongPasswordException("Pass length must be more than 4");
        validateCharacters(pass);
        this.pass = pass;
    }

    private void validateCharacters(String pass) throws WrongPasswordException {
        for (int i = 0; i < pass.length(); i++) {
            if (!Character.isDigit(pass.charAt(i)) && !Character.isLetter(pass.charAt(i)))
                throw new WrongPasswordException("Password must contain only digits and letters");
        }
    }

    public boolean compare(int anotherPassHash) {
        return (this.hashCode() == anotherPassHash);
    }

    @Override
    public int hashCode() {
        return pass != null ? pass.hashCode() : 0;
    }
}
