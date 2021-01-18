package oop.ex6.main.tree;

import java.util.HashMap;


/**
 * This class decrible the root of the tree
 * the root contain the hash that contain the methods name
 */
public class Root extends TreeNode {

    private HashMap<String, Method> methods;


    // The parent of the root is null
    public Root() {
        setParent(null);
        methods = new HashMap<String, Method>();
    }

    /**
     * THis method add method to the root
     *
     * @param method method
     * @return true if the hash doesnt contain this method, false otherwise
     */
    public boolean addMethod(Method method) {
        if (methods.containsKey(method.getMethodName())) {
            return false;
        }
        methods.put(method.getMethodName(), method);
        method.setParent(this);
        return true;
    }

    /**
     * @return This method return the hashmap method
     */
    public HashMap<String, Method> getMethods() {
        return methods;
    }


}
