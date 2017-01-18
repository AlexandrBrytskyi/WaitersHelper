package server.service;

/**
 * Created by SashoK on 10.03.2016.
 */
public class TestEnum {

    public static void main(String[] args) {
        System.out.println(Enum1.TEST.equals(Enum2.TEST));
        System.out.println(Enum1.TEST.hashCode()==Enum2.TEST.hashCode());

    }
}
