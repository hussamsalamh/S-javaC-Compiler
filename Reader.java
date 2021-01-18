package oop.ex6.main;

import oop.ex6.main.exception.MyException;
import oop.ex6.main.tree.Method;
import oop.ex6.main.tree.MyNode;
import oop.ex6.main.tree.Root;
import oop.ex6.main.tree.Scope;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class read the file
 * This class contain two main method
 * firstRead method : read the file for first time and set the global variable and set the method name
 * and save the file to fileArray
 * secondRead method : read from file array, start from method object startLine
 */
public class Reader {
    //root
    private Root root;
    //Path of file
    private String path;
    //File array
    private ArrayList<String> fileArray;
    private int counter;
    private boolean inMethod;
    private boolean isReturn;

    public Reader(String path) {
        setPath(path);
        fileArray = new ArrayList<String>();
        counter = 0;
        inMethod = false;
        isReturn = false;
        root = new Root();
    }

    /**
     * This method read the file for first time and set the global variable
     * and the method name and the argument for the method
     *
     * @throws Exception throw exception if theres error ( no return , syntax error , final not declare)
     */
    public void firstRead() throws Exception {
        try {
            File file = new File(path);
            Scanner myReader = new Scanner(file);
            int lineNumber = 0;
            Method method = null;
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                fileArray.add(line);
                lineNumber++;
                if ((line.trim().equals("") || Checker.isComment(line))) {
                    continue;
                }
                if (inMethod) {             // If we read inside the method
                    if (isReturn) {
                        // This mean the return must be the last code line inside the method else will
                        // make it isReturn false
                        if (!(line.trim().equals("") || Checker.isComment(line)
                                || Checker.isCloseScope(line))) {
                            isReturn = false;
                        }
                    }
                    if (Checker.isVoid(line)) {     // Check if theres void inside the method <Error>
                        throw new MyException(lineNumber, MyException.SYNTAX_ERROR);
                    }
                    if (Checker.isOpenScope(line)) {    // This to check the number of brackets
                        counter++;
                    }
                    if (Checker.isReturn(line) && counter == 1) {
                        isReturn = true;
                        continue;
                    }
                    if (Checker.isCloseScope(line)) {
                        if (isReturn && counter == 1) {
                            counter--;
                            inMethod = false;
                            assert method != null;
                            method.setEndLine(lineNumber - 1);
                        } else {
                            counter--;
                        }
                    }
                    // That's mean theres not return in the method
                    if (counter == 0 && inMethod) {
                        throw new MyException(lineNumber - 1, MyException.NO_RETURN);
                    }
                } else {
                    // If its void then its mean its method
                    if (Checker.isVoid(line)) {
                        // Check the method line
                        method = Checker.checkMethodLine(line, root);
                        if (method == null) {
                            throw new MyException(lineNumber, Checker.getErrorType());
                        } else {
                            method.setStartLine(lineNumber);
                            inMethod = true;
                            counter++;
                        }
                    } else {
                        // If the code line not start with void then its variable
                        // for the comment and empty line will skip it in the first code
                        // This check the variable
                        if (!Checker.checkVariableLine(line, root)) {
                            throw new MyException(lineNumber, Checker.getErrorType());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // If the exception is from file print 2 and exit
            if (e instanceof FileNotFoundException) {
                System.out.println(2);
                System.exit(2);
            }
            //Else throw new exception with the error message
            throw new MyException(e.getMessage());
        }
        if (counter != 0) {
            throw new MyException(MyException.BRACKET_ERROR);
        }
    }

    /**
     * This method read from fileArray and check the data
     *
     * @throws Exception throw exception if theres error inside the method
     */
    public void secondRead() throws Exception {
        // Iteration over methods from hashMap methods
        for (Map.Entry<String, Method> entry : root.getMethods().entrySet()) {
            Method method = entry.getValue();
            // This to add new scope for the method new child for method
            Scope tempNode = new Scope();
            method.addScope(tempNode);
            MyNode scope = tempNode;
            scope.setVariables(method.getVariables()); // add the argument for the scope
            for (int i = method.getStartLine(); i < method.getEndLine(); i++) {
                // If the line end with {  then add new scope
                if (Checker.isOpenScope(fileArray.get(i))) {
                    Scope temp = new Scope();
                    scope.addScope(temp);
                    scope = temp;
                }
                // If the line start with } scope will be the parent
                if (Checker.isCloseScope(fileArray.get(i))) {
                    scope = (MyNode) scope.getParent();
                    scope.getScopes().pop();
                    continue;
                }
                // This section to check the data inside the method
                if (!(readInsideMethod(fileArray.get(i), scope))) {
                    throw new MyException(i + 1, Checker.getErrorType());
                }
            }
        }
    }

    /**
     * This method check the data inside the method
     *
     * @param line  the method line
     * @param scope the current scope
     * @return true if every thing okay, false otherwise ( Errors )
     */
    public boolean readInsideMethod(String line, MyNode scope) {
        // If the line is empty or return or comment the return true
        if (line.trim().equals("") || Checker.isComment(line) || Checker.isReturn(line)) {
            return true;
        }
        // This to check if/while line
        if (Checker.isItIfOrWhile(line)) {
            return Checker.CheckIfWhileLine(scope);
        }
        // This to check calling method inside method
        if (Checker.callingMethod(line)) {
            return Checker.checkCallingMethod(root, scope);
        }
        // If none of above , the line will be variable
        return Checker.checkVariableLine(line, scope);
    }


    /**
     * THis method set tha path of the file
     *
     * @param path path of the file
     */
    public void setPath(String path) {
        this.path = path;
    }

}
