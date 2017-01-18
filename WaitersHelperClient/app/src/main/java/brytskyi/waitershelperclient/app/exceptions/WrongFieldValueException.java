package brytskyi.waitershelperclient.app.exceptions;

/**
 * Created by huyti on 22.05.2016.
 */
public class WrongFieldValueException extends Exception {

    public WrongFieldValueException(String detailMessage) {
        super("Wrong field value " + detailMessage);
    }
}
