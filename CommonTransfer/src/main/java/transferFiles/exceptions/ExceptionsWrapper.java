package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 15.05.2016
 */
public class ExceptionsWrapper {

    private Class<? extends Exception> excClass;
    private String message;

    public ExceptionsWrapper() {
    }

    public ExceptionsWrapper(Class<? extends Exception> excClass, String message) {
        this.excClass = excClass;
        this.message = message;
    }

    public Class<? extends Exception> getExcClass() {
        return excClass;
    }

    public void setExcClass(Class<? extends Exception> excClass) {
        this.excClass = excClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
