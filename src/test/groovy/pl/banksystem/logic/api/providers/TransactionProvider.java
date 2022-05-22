package pl.banksystem.logic.api.providers;

public class TransactionProvider {
    public static String getTransaction1(){
        String s = "{\n" +
                "  \"transactionID\" : null,\n" +
                "  \"transactionType\" : \"Deposit\",\n" +
                "  \"transactionHistory\" : [ {\n" +
                "    \"transactionStatusType\" : \"PENDING\",\n" +
                "    \"changeDate\" : 1653174124013\n" +
                "  } ],\n" +
                "  \"amount\" : 10.0,\n" +
                "  \"accountID\" : 1,\n" +
                "  \"transactionDate\" : 1653174124013,\n" +
                "  \"actualTransactionStatus\" : {\n" +
                "    \"transactionStatusType\" : \"PENDING\",\n" +
                "    \"changeDate\" : 1653174124013\n" +
                "  }\n" +
                "}";
        return s;
    }
    public static String getTransaction2(){
        String s = "{\n" +
                "  \"transactionID\" : null,\n" +
                "  \"transactionType\" : \"Deposit\",\n" +
                "  \"transactionHistory\" : [ {\n" +
                "    \"transactionStatusType\" : \"PENDING\",\n" +
                "    \"changeDate\" : 1251174124023\n" +
                "  } ],\n" +
                "  \"amount\" : 200.0,\n" +
                "  \"accountID\" : 2,\n" +
                "  \"transactionDate\" : 1653174124013,\n" +
                "  \"actualTransactionStatus\" : {\n" +
                "    \"transactionStatusType\" : \"PENDING\",\n" +
                "    \"changeDate\" : 1251174124023\n" +
                "  }\n" +
                "}";
        return s;
    }
    public static String getTransaction3(){
        String s = "{\n" +
                "  \"transactionID\" : null,\n" +
                "  \"transactionType\" : \"Deposit\",\n" +
                "  \"transactionHistory\" : [ {\n" +
                "    \"transactionStatusType\" : \"PENDING\",\n" +
                "    \"changeDate\" : 1653218731757\n" +
                "  }, {\n" +
                "    \"transactionStatusType\" : \"CANCELED\",\n" +
                "    \"changeDate\" : 1653218731775\n" +
                "  } ],\n" +
                "  \"amount\" : 2000.0,\n" +
                "  \"accountID\" : 1,\n" +
                "  \"transactionDate\" : 1653218731757,\n" +
                "  \"actualTransactionStatus\" : {\n" +
                "    \"transactionStatusType\" : \"CANCELED\",\n" +
                "    \"changeDate\" : 1653218731775\n" +
                "  }\n" +
                "}";
        return s;
    }
}
