package ru.praktika95;

public class Main {

    private String str = "";
    private static String str2 = "";
    private final String str3 = "";
    private static final String str4 = "";
    private volatile String str5 = "";
    private transient String str6 = "";

    public static void main(String[] args) {
        int a = 5;
        long b = 5L;
        char c = 'a';
        boolean bo = true;
        float f = 0.0f;
        double d = 0.0;
        String s = "asdfg";

        method(a);

//        if (true){
//
//        }
//
//        for (int i = 0; i < 5; i++){
//
//        }
//
//        do {
//
//        } while (true);

        System.out.println("Hello, world!");
    }

    void a() {};

    private static void method(int a){
        a = 77;
    }
}
