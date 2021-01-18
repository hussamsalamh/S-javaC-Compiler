package oop.ex6.main.tree;


/**
 * This class describe the global and local variable
 * its contain an information of the variable <name,isFinal,isInitialized,type>
 */
public class Variable {
    private String variableName;
    private String variableType;
    private boolean isInitialized;
    private boolean isFinal;


    public Variable() {
    }

    // This method get the name of the variable
    public String getVariableName() {
        return variableName;
    }


    /**
     * This method set if the variable is initialized
     * @param initialized true if variable its initialized, false otherwise
     */
    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    /**
     * @return This method return true if the variable is initialized, false otherwise
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     *
     * @return return the type of the variable
     */
    public String getVariableType() {
        return variableType;
    }

    /**
     *
     * @return true if the variable is final,false otherwise
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * This method to set if the variable is final or not
     * @param aFinal true if the variable is final, false otherwise
     */
    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    /**
     * This method set the variable name
     * @param variableName variable name
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * This method set the variable type
     * @param variableType the variable type
     */
    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }


}
