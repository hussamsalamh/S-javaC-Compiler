package oop.ex6.main.tree;
import java.util.LinkedList;



/**
 * This class describe all the vertices ( node )  in the tree
 * This class extend from TreeNode
 * So its have hashMap for variables
 */
public class MyNode extends TreeNode {

    public LinkedList<Scope> scopes;    // LinkedList for the scopes


    public MyNode() {
        super(); // Super for HashMap variables
        scopes = new LinkedList<>();
    }

    /**
     * This method add new scope to the linkedList and set the parent to the new scope
     *
     * @param scope new scope
     */
    public void addScope(Scope scope) {
        scopes.add(scope);      // current scopes will add scope
        scope.setParent(this);  // the new scope the parent of it will be scopes
    }

    /**
     * @return scopes
     */
    public LinkedList<Scope> getScopes() {
        return scopes;
    }

}
