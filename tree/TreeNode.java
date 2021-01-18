package oop.ex6.main.tree;
import java.util.HashMap;

/**
 * This class the base of the tree
 * This class have hash the contain all the global variable
 *
 * The root class is extend from treenode class
 */

public class TreeNode {
    private HashMap<String, Variable> variables;
    private TreeNode parent;

    public TreeNode() {
        variables = new HashMap<String, Variable>();
    }

    /**
      * @return the parent of the node
     */
    public TreeNode getParent() {
        return parent;
    }


    /**
     * This method set the parent of the node
     * @param parent parent of the node
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * This method add the variable to the hash
     * @param variable variable
     * @return true if the hash doesnt have the variable, false otherwise
     */
    public boolean addVariable(Variable variable) {
        if (variables.get(variable.getVariableName()) != null){
            return false;
        }
        variables.put(variable.getVariableName(), variable);
        return true;
    }


    /**
     * This method get hash that contain the global variable
     * @return variables ( hash map that contain the variable )
     */
    public HashMap<String, Variable> getVariables() {
        return variables;
    }

    /**
     * This method set the variables
     * @param variables hashmap contain variable
     */
    public void setVariables(HashMap<String, Variable> variables) {
        this.variables = variables;
    }
}
