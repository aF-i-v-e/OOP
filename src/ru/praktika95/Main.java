package ru.praktika95;

public class Main {
    private String str="af";
    private final String str2="5";//это типа константы
    private static final String constStr="str";
    private volatile String str5="многопоточность";
    private  transient String str6="сериализация для долгого сохранения в памяти или передачи данных";
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        int a=0;
        long h=9;
        char ch='a';
        boolean g=true;
        var c=9;
        float r=78.0f;
        String s="stroka";
        method(a);
        if (true) {

        }
        for (int i=0;i<5;i++){
            System.out.print(s);
        }
    }
    private static void method(int a){
        a=77;
    }
    void m(){
        //данный метод доступен всему пакету
        //буз модификатора доступа видны всему пакету
    }
}
