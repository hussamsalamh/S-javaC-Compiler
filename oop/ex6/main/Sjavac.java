package oop.ex6.main;

public class Sjavac {
    final static Integer ONE = 1;
    final static Integer ZERO = 0;

    public static void main(String[] args)  {
        if (args.length != ONE) {
            return;
        }
        Compiler.doAction(args[ZERO]);
    }
}
