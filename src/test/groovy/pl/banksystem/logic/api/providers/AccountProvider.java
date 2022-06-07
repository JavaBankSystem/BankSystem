package pl.banksystem.logic.api.providers;

public class AccountProvider {

    public static String account1(){
        return "{\n" +
                "  \"accountID\" : null,\n" +
                "  \"client\" : {\n" +
                "    \"id\" : 1,\n" +
                "    \"username\" : \"test\",\n" +
                "    \"password\" : \"password\",\n" +
                "    \"roles\" : null,\n" +
                "    \"accounts\" : null\n" +
                "  },\n" +
                "  \"balance\" : 1500.0\n" +
                "}";
    }
    public static String account2(){
        return "{\n" +
                "  \"accountID\" : null,\n" +
                "  \"client\" : {\n" +
                "    \"id\" : 2,\n" +
                "    \"username\" : \"test\",\n" +
                "    \"password\" : \"password\",\n" +
                "    \"roles\" : null,\n" +
                "    \"accounts\" : null\n" +
                "  },\n" +
                "  \"balance\" : 1800.0\n" +
                "}";
    }


}
