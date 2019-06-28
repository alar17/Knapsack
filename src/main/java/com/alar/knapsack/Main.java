package com.alar.knapsack;

import com.typesafe.config.ConfigException.NotResolved;

public class Main {
    public static void main(String[] args) {
        try {                                                             // NOTEST
            new Directives();
        } catch (NotResolved x) {                                                                         // NOTEST
            System.err.println("Error: no external configuration specified.");                            // NOTEST
            System.err.println("Please include a -Dconfig.file=f.conf system property and try again.");   // NOTEST
            System.exit(2);                                                                               // NOTEST
        } catch (Exception x) {
            x.printStackTrace(System.err);
            System.exit(1);
        }
        System.out.println("Server started - Call Knapsack");
    }

}
