package com.alar.knapsack;

public class Main {
    public static void main(String[] args) {
        try {
            new Directives();
        } catch (Exception x) {
            x.printStackTrace(System.err);
            System.exit(1);
        }
        System.out.println("Server started - Call Knapsack");
    }
}
