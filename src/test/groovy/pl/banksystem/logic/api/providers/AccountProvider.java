package pl.banksystem.logic.api.providers;

public class AccountProvider {

    public static String account1(){
        String s = "{\n" +
                "  \"accountId\" : null,\n" +
                "  \"clientID\" : 1,\n" +
                "  \"balance\" : 1500.0\n" +
                "}";
        return s;
    }
    public static String account2(){
        String s = "{\n" +
                "  \"accountId\" : null,\n" +
                "  \"clientID\" : 2,\n" +
                "  \"balance\" : 1800.0\n" +
                "}";
        return s;
    }


}
