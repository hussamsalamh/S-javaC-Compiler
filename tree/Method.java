package oop.ex6.main.tree;
import java.util.LinkedList;

/**
 * This class describe the method object
 * This class extend from MyNode --> MyNode extend from TreeNode
 */
public class Method extends MyNode {
    private int startLine;  // First line in the method
    private int endLine;    // The end line of method
    private LinkedList<Variable> variableList;
    private String methodName;


    public Method(String methodName) {
        setMethodName(methodName);
        variableList = new LinkedList<Variable>();
    }


    /**
     * This method add the variable to hashmap variable and for linkedList
     * For hashMap to check if the variable in the method Time complexity O(1)
     * LinkedList the oreder of the arguments
     *
     * @param variable variable new variable ( arguments )
     * @return true if add the argument, false otherwise
     */
    @Override
    public boolean addVariable(Variable variable) {
        if (!super.addVariable(variable)) {
            return false;
        }
        variableList.add(variable);
        return true;
    }

    /**
     * @return the variable in the method
     */
    public LinkedList<Variable> getVariableList() {
        return variableList;
    }


    /**
     * This method add the argument to the method
     *
     * @param variables variables
     * @return true if added successfully, false otherwise
     */
    public boolean addArgumentsToVariables(LinkedList<Variable> variables) {
        for (Variable variable : variables) {
            if (!(addVariable(variable))) {
                return false;
            }
        }
        return true;
    }

    // This method return the method name
    public String getMethodName() {
        return methodName;
    }

    // This method set the name of the method object
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    // This method get the endLine of the method
    public int getEndLine() {
        return endLine;
    }

    // THis method get the start line of the method
    public int getStartLine() {
        return startLine;
    }

    /**
     * This method set the endLine of the method
     *
     * @param endLine end line for method object
     */
    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }


    /**
     * This method set the start line for the method
     *
     * @param startLine start line of the method
     */
    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

}
