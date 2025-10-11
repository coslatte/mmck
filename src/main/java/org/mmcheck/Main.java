package org.mmcheck;

public class Main {
    static void main() {
        Cli cli = new Cli();

        if (cli.init()) {
            System.out.println(":D");
        } else {
            System.exit(0);
        }
    }
}


