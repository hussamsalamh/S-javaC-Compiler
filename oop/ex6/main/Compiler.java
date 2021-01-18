package oop.ex6.main;

/**
 * This class Print 0 if theres not Error in the code file
 * otherwise its print 1 and the error msg and exit from the program
 */
public abstract class Compiler {

    private static final int ONE = 1;
    private static final int ZERO = 0;

    /**
     * This method call for the firstRead method that check the global variable
     * and check the method name and there argument and check if the method
     * contain return in the last line
     * and save them to hashMap for variable and HashMap for method
     * <p>
     * <p>
     * This method call the secondRead to check the code inside the method
     * check if/while line and check the local variable
     *
     * @param path The path of the file
     */
    public static void doAction(String path) {
        Reader reader = new Reader(path);
        try {
            reader.firstRead();
        } catch (Exception e) {
            System.out.println(ONE);
            System.err.println(e.getMessage());
            return;
        }
        try {
            reader.secondRead();
        } catch (Exception e) {
            System.out.println(ONE);
            System.err.println(e.getMessage());
            return;
        }
        System.out.println(ZERO);
    }
}

