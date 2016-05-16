package transferFiles.exceptions;


public class ExceptionUtils {


    public static void throwException(Class<? extends Exception> excClass, String message) throws Exception{
        if (excClass.equals(AccountBlockedException.class)) throw new AccountBlockedException(message);
        if (excClass.equals(DenominationWithIdNotFoundException.class))
            throw new DenominationWithIdNotFoundException(message);
        if (excClass.equals(IngridientWithIDNotFoundException.class))
            throw new IngridientWithIDNotFoundException(message);
        if (excClass.equals(NoDishWithIdFoundedException.class))
            throw new NoDishWithIdFoundedException(message);
        if (excClass.equals(NoOrderingWithIdException.class))
            throw new NoOrderingWithIdException(message);
        if (excClass.equals(OrderingAlreadyServingException.class))
            throw new OrderingAlreadyServingException(message);
        if (excClass.equals(OrderingNotServingByYouException.class))
            throw new OrderingNotServingByYouException(message);
        if (excClass.equals(ProductByIdNotFoundException.class))
            throw new ProductByIdNotFoundException(message);
        if (excClass.equals(UserAccessException.class))
            throw new UserAccessException(message);
        if (excClass.equals(UserFieldIsEmptyException.class))
            throw new UserFieldIsEmptyException(message);
        if (excClass.equals(WrongLoginException.class))
            throw new WrongLoginException(message);
        if (excClass.equals(WrongPasswordException.class))
            throw new WrongPasswordException(message);
    }

}
