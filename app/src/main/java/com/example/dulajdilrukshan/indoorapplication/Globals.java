package com.example.dulajdilrukshan.indoorapplication;

/**
 * Created by Oshan Fernando on 8/29/2018.
 */

public class Globals {
    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private String username;


    private Globals() {

    }


    public String getValue() {
        return username;
    }


    public void setValue(String name) {
        this.username = name;
    }
}
