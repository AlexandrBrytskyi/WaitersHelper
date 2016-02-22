package server.validator;


/*interface has only one method, which must be realised by UI, such that when we create UI, we send
* to the validator UI odject */
public interface Loginable {

    void sendUIToLoginedList();
}
