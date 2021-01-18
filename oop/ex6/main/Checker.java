package oop.ex6.main;

import oop.ex6.main.exception.MyException;
import oop.ex6.main.tree.*;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class check the code and set the error msg if theres error
 */
public abstract class Checker {
    // Theres only one matcher and one pattern ( and there are linked in most of method )

    private static final int ZERO = 0;
    private static Pattern pattern;
    private static Matcher matcher;
    private static String errorType;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// Check oop.ex6.main.tree.Method line  ///////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method check the pattern of the method
     *
     * @param methodLine method line in the code
     * @return return true if the methodLine match the method pattern , otherwise false
     */
    private static boolean checkMethodPattern(String methodLine) {
        // this check the pattern of method line its good or not
        // check if contain " void nameOfMethod ( argumentLine ) { " return true
        pattern = Pattern.compile(Patterns.METHOD_LINE_PATTERN.getValue());
        matcher = pattern.matcher(methodLine);
        return matcher.find();
    }


    /**
     * This method check the method < name of the method >
     * <if theres a open brackets " open scope" >
     *
     * @param methodLine method line
     * @param root       the root
     * @return method object
     */
    public static Method checkMethodLine(String methodLine, Root root) {
        if (!(checkMethodPattern(methodLine))) {
            setErrorType(MyException.SYNTAX_ERROR);
            return null;
        }
        // Already check on checkMethodPattern
        String name = matcher.group(2).trim();
        Method method = new Method(name);
        // matcher.group(4) is the argumentLine
        LinkedList<Variable> arguments;
        try {
            arguments = checkMethodArguments(matcher.group(3));
        } catch (Exception c) {
            arguments = new LinkedList<>();
        }
        if (arguments == null) {
            return null;
        } else {
            if (!(method.addArgumentsToVariables(arguments))) {
                setErrorType(MyException.DOUBLE_DECLARE);
                return null;
            }
        }
        if (!(root.addMethod(method))) {
            setErrorType(MyException.DOUBLE_DECLARE);
            return null;
        }
        return method;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// Check the argument Line ///////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * void methodName ( arguments    ) {
     * This method check the argument line
     *
     * @param argumentLine argument line
     * @return LinkedList that have the argument
     */
    private static LinkedList<Variable> checkMethodArguments(String argumentLine) {
        //int a , int c, final String b
        LinkedList<Variable> argumentList = new LinkedList<Variable>();
        String[] arguments = argumentLine.trim().split(Patterns.SYMBOL_PATTERN.getValue());
        if (argumentLine.trim().length() != 0) {
            int howMany = countChar(argumentLine);
            if (arguments.length != howMany + 1) {
                setErrorType(MyException.ARGUMENT_ERROR);
                return null;
            }
        } else {
            return argumentList;
        }
        for (String argument : arguments) {
            Variable variable = checkArgument(argument);
            if (variable == null) {
                setErrorType(MyException.ARGUMENT_ERROR);
                return null;
            }
            argumentList.add(variable);
        }
        return argumentList;
    }

    /**
     * This method check the argument and build new variable object
     *
     * @param argument argument
     * @return variable if every things okay , null otherwise
     */
    private static Variable checkArgument(String argument) {
        // This method check if argument final int b
        // Final int b = 10 ; --- > final group 1 , int group 3, b  group 4
        boolean isFinal = false;
        pattern = Pattern.compile(Patterns.ARGUMENT_DECLARATION_PATTERN.getValue());
        matcher = pattern.matcher(argument);
        if (!matcher.find()) {
            return null;
        }
        try {
            //If the matcher have group 1 then its final
            matcher.group(1);
            if (matcher.group(1) != null) {
                isFinal = true;
            }
        } catch (IllegalStateException ignored) {
        }
        String type = matcher.group(3);
        String name = matcher.group(4).trim();
        if (!(checkVariableName(name))) {   // Check the variable name
            setErrorType(argument);
            return null;
        }
        // This to set the variable
        return setVariable(name, type, isFinal, true);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////// oop.ex6.main.tree.Variable Line /////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * This method the check the variable line global and local variable
     *
     * @param variableLine variable line
     * @param root         root, or can be scope
     * @return true if there's not error in the line, false otherwise
     */
    public static boolean checkVariableLine(String variableLine, TreeNode root) {
        boolean isFinal = isFinal(variableLine);    // Check if its final
        if (isFinal) {
            variableLine = variableLine.trim().substring(5).trim(); // remove the final word

        }
        // Set type
        String type = getType(variableLine);
        String variablesLine;
        try {
            variablesLine = matcher.group(2);
        } catch (Exception e) {
            variableLine = variableLine.trim();
            if (variableLine.charAt(variableLine.length() - 1) != ';') {
                setErrorType(MyException.UNKNOWN_LINE);
                return false;
            }
            variablesLine = variableLine.trim().substring(0, variableLine.trim().length() - 1);
        }
        int howMany = countChar(variablesLine);
        String[] variables = variablesLine.trim().split(Patterns.SYMBOL_PATTERN.getValue());
        if (variables.length != howMany + 1) {
            setErrorType(MyException.SYNTAX_ERROR);
            return false;
        }
        if (type == null && isFinal) {
            setErrorType(MyException.SYNTAX_ERROR);
            return false;
        }
        for (String nameAndValue : variables) {
            if (!setVariable(nameAndValue, type, isFinal, root)) {
                return false;
            }

        }
        return true;
    }

    /**
     * THis method add the variable
     *
     * @param name          name of variable
     * @param type          the type of variable
     * @param isFinal       is it final
     * @param isInitialized is it initialized
     * @param treeNode      root in case global variable, scope in case of local
     * @return true if theres not error, false otherwise
     */
    private static boolean setVariableToTree(String name, String type, boolean isFinal
            , boolean isInitialized, TreeNode treeNode) {
        // Check if can add it to treeNode
        if (!treeNode.addVariable(setVariable(name, type, isFinal, isInitialized))) {
            setErrorType(MyException.DOUBLE_DECLARE);
            return false;
        }
        return true;

    }

    /**
     * This method take nameAndValue and check if its correct b = 10 or b
     *
     * @param nameAndValue name and value
     * @param type         the type of variable
     * @param isFinal      is it final
     * @param treeNode     treeNode
     * @return true if theres not any error, false otherwise
     */
    private static boolean setVariable(String nameAndValue, String type,
                                       boolean isFinal, TreeNode treeNode) {
        boolean isInitialized = false;
        String[] valueName = nameAndValue.trim().split("=");
        if (valueName.length != 2) {
            if (isFinal) {  // That's mean the final  variable is not initialized
                setErrorType(MyException.SYNTAX_ERROR);
                return false;
            }
        }
        // Check variable name
        if (!(checkVariableName(valueName[0].trim()))) {
            setErrorType(MyException.SYNTAX_ERROR);
            return false;
        }
        // If type is null that's mean ( a = b ) casting or declaration by another variable
        if (type == null) {
            // Getting variable from treeNode
            Variable variable = getVariableFromTree(valueName[0].trim(), treeNode);
            if (variable == null) {     // That's mean theres no variable in that name
                setErrorType(MyException.PARAMETER_DOESNT_EXIST);
                return false;
            }
            if (variable.isFinal()) {       // If the variable is final cant change his value
                setErrorType(MyException.FINAL_DECLARE);
                return false;
            }
            //  check if a = 10
            if (checkValue(valueName[0], valueName[1], variable.getVariableType(), treeNode)) {
                variable.setInitialized(true);
                return true;
            }
        }
        try {
            if (!(checkValue(valueName[0], valueName[1].trim(), type, treeNode))) {
                return false;
            } else {
                isInitialized = true;
            }
        } catch (Exception ignored) {
        }
        return setVariableToTree(valueName[0].trim(), type, isFinal, isInitialized, treeNode);
    }


    /**
     * This method get the type of the variable from the line
     *
     * @param variableLine variable line
     * @return string the type
     */
    private static String getType(String variableLine) {
        pattern = Pattern.compile(Patterns.NON_FINAL_VARIABLE.getValue());
        matcher = pattern.matcher(variableLine);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////// Check Value ////////////////////////////////////////////////////////
    /////////////////////////////These method for final and non-final variable//////////////////////
    /////////////////////////// Its work for global and local variable /////////////////////////////

    /**
     * This method check if the value match the variable
     *
     * @param variableName variable name
     * @param value        value
     * @param type         type of variable
     * @param treeNode     treeNode
     * @return true if its match, false otherwise
     */
    private static boolean checkValue(String variableName, String value,
                                      String type, TreeNode treeNode) {
        // This in case if value like this ( 10 , "dsadas" , 'd' ,true)
        if (valueMatchType(value, type)) {
            return true;
        }
        // THis check if can make cast, In case a = b
        return checkCasting(variableName, value, treeNode, type);
    }

    /**
     * This method get the variable from treeNode, or scope
     *
     * @param variableName variable name
     * @param treeNode     treeNode < can be scope >
     * @return variable if theres variable in treeNode, null otherwise
     */
    private static Variable getVariableFromTree(String variableName, TreeNode treeNode) {

        if (treeNode == null) {
            return null;
        }
        // This for global variable
        if (treeNode.getParent() == null) {
            if (treeNode.getVariables().containsKey(variableName)) {
                return treeNode.getVariables().get(variableName);
            }
            return null;
        }
        // This for local variable
        TreeNode temp = treeNode;
        while (temp != null) {
            if (temp.getVariables().containsKey(variableName)) {
                return temp.getVariables().get(variableName);
            }
            temp = temp.getParent();
        }
        return null;
    }

    /**
     * This method check if can cast
     *
     * @param nameVariable variable name
     * @param value        value
     * @param treeNode     treeNode
     * @param type         type
     * @return true if can cast, false otherwise
     */
    private static boolean checkCasting(String nameVariable, String value, TreeNode treeNode,
                                        String type) {
        //Mean the the variableName have type "  int a = b " cant be int a = 10 (1)
        // (1) this have been check in the valueMatchType method
        Variable variable1 = getVariableFromTree(value, treeNode);
        // If b was not define in the previous scope
        if (variable1 == null) {
            setErrorType(MyException.VARIABLE_VALUE_ERROR);
            return false;
        }
        if (type == null) {
            // This section is for line a = b
            Variable variable = getVariableFromTree(nameVariable, treeNode);
            if (variable == null) { //Check if the variable has been define
                setErrorType(MyException.PARAMETER_DOESNT_EXIST);
                return false;
            }
            //Getting the type from the variable
            type = variable.getVariableType();
            if (variable.isFinal()) {       //Check if the variable are final
                setErrorType(MyException.FINAL_DECLARE);
                return false;
            }
            //This if the line is a = 10 , to check if the value is not variable
            if (valueMatchType(value, variable.getVariableType())) {
                return true;
            }
            if (canMakeCasting(type, variable1)) {
                variable.setInitialized(true);
                return true;
            }
            setErrorType(MyException.VALUE_ERROR);
            return false;
        }
        //Check if the value have been initialized
        if (!variable1.isInitialized()) {
            setErrorType(MyException.PARAMETER_HAS_NOT_INITIALIZED);
            return false;
        }
        // Check if can be cast
        return canMakeCasting(type, variable1);
    }

    /**
     * This method check if the two type of variable
     * a = b --- > can declare from type of b
     *
     * @param variableType variable type a
     * @param variable1    variable      b
     * @return true if can make cast, false otherwise
     */
    private static boolean canMakeCasting(String variableType, Variable variable1) {
        switch (variableType) {
            case Patterns.BOOLEAN:
                switch (variable1.getVariableType()) {
                    case Patterns.BOOLEAN:
                    case Patterns.DOUBLE:
                    case Patterns.INT_VALUE:
                        return true;
                    default:
                        return false;
                }
            case Patterns.DOUBLE:
                switch (variable1.getVariableType()) {
                    case Patterns.DOUBLE:
                    case Patterns.INT_VALUE:
                        return true;
                    default:
                        return false;
                }
            case Patterns.INT_VALUE:
                return Patterns.INT_VALUE.equals(variable1.getVariableType());
            case Patterns.STRING:
                return Patterns.STRING.equals(variable1.getVariableType());
            case Patterns.CHAR:
                return Patterns.CHAR.equals(variable1.getVariableType());
            default:
                return false;
        }
    }

    /**
     * This method check if the value match the type of variable
     *
     * @param value value --> (10,"dsa",true)
     * @param type  type
     * @return true if match, false otherwise
     */
    private static boolean valueMatchType(String value, String type) {
        switch (type) {
            case Patterns.INT_VALUE:
                return isInteger(value);
            case Patterns.DOUBLE:
                return isDouble(value);
            case Patterns.BOOLEAN:
                return isBoolean(value);
            case Patterns.CHAR:
                return isChar(value);
            case Patterns.STRING:
                return isString(value);
        }
        return false;
    }

    /**
     * This method check The variable name
     *
     * @param name name
     * @return true if its okay, false otherwise
     */
    private static boolean checkVariableName(String name) {
        // This method check the variableName
        if (name.length() == 1) {
            pattern = Pattern.compile(Patterns.VARIABLE_NAME_PATTERN2.getValue());
            matcher = pattern.matcher(name);
            return matcher.find();
        }
        pattern = Pattern.compile(Patterns.VARIABLE_NAME_PATTERN.getValue());
        matcher = pattern.matcher(name);
        return matcher.find();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////Value Match Type /////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param value value
     * @return true if the value integer
     */
    private static boolean isInteger(String value) {
        pattern = Pattern.compile(Patterns.INTEGER_PATTER.getValue());
        matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * @param value value
     * @return true if the value double
     */
    private static boolean isDouble(String value) {
        pattern = Pattern.compile(Patterns.DOUBLE_PATTERN.getValue());
        matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * @param value value
     * @return true if the value String
     */
    private static boolean isString(String value) {
        pattern = Pattern.compile(Patterns.STRING_PATTERN.getValue());
        matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * @param value value
     * @return true if the value char
     */
    private static boolean isChar(String value) {
        pattern = Pattern.compile(Patterns.CHAR_PATTERN.getValue());
        matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * @param value value
     * @return true if the value boolean
     */
    private static boolean isBoolean(String value) {
        pattern = Pattern.compile(Patterns.BOOLEAN_PATTERN.getValue());
        matcher = pattern.matcher(value);
        return matcher.find();
    }


    /**
     * @param line line
     * @return true if the line contain void
     */
    public static boolean isVoid(String line) {
        pattern = Pattern.compile(Patterns.VOID_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }


    /**
     * @param variableLine variableLine
     * @return true if variable line contain final
     */
    private static boolean isFinal(String variableLine) {
        // This method check if the variable is final
        pattern = Pattern.compile(Patterns.FINAL_VARIABLE.getValue());
        matcher = pattern.matcher(variableLine);
        return matcher.find();
    }

    /**
     * @param line line
     * @return true if the line contain return
     */
    public static boolean isReturn(String line) {
        pattern = Pattern.compile(Patterns.RETURN_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * @param line line
     * @return true if the line contain }
     */
    public static boolean isCloseScope(String line) {
        pattern = Pattern.compile(Patterns.CLOSE_SCOPE_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * @param line line
     * @return true if the line contain if/while
     */
    public static boolean isItIfOrWhile(String line) {
        pattern = Pattern.compile(Patterns.IF_WHILE_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * @param line line
     * @return true if the line calling method
     * nameMethod ( argument) ;
     */
    public static boolean callingMethod(String line) {
        pattern = Pattern.compile(Patterns.CALLING_METHOD_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    /**
     * @param line line
     * @return true if the line contain {
     */
    public static boolean isOpenScope(String line) {
        pattern = Pattern.compile(Patterns.OPEN_SCOPE_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }


    /**
     * This method check if the line is comment
     *
     * @param line line
     * @return true if its comment, false otherwise
     */
    public static boolean isComment(String line) {
        pattern = Pattern.compile(Patterns.COMMENT_PATTERN.getValue());
        matcher = pattern.matcher(line);
        return matcher.find();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// Check inside method  ///////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////If While Line

    /**
     * This method check if/while line
     *
     * @param treeNode    treeNode
     * @return true if theres not error, false otherwise
     */
    public static boolean CheckIfWhileLine( TreeNode treeNode) {
        String[] values = matcher.group(2).split(Patterns.SPLIT_CONDITION.getValue());
        for (String value : values) {
            if (isBoolean(value.trim())) {  // Check if value ( number , boolean)
                continue;
            }
            // In case if inside the condition its variable
            Variable variable = getVariableFromTree(value.trim(), treeNode);
            if (variable == null) {
                setErrorType(MyException.IF_WHILE_ERROR);
                return false;
            }
            // If the variable isn't initialized
            if (!variable.isInitialized()) {
                setErrorType(MyException.PARAMETER_HAS_NOT_INITIALIZED);
                return false;
            }
            if (!(canMakeCasting(Patterns.BOOLEAN, variable))) {
                setErrorType(MyException.IF_WHILE_ERROR);
                return false;
            }
        }
        return true;
    }


    ////////////////// Calling method

    /**
     * This method check if can calling the method
     *
     * @param root  root
     * @param scope scope
     * @return true if can call this method , false otherwise
     */
    public static boolean checkCallingMethod(Root root, MyNode scope) {
        Method method = root.getMethods().get(matcher.group(1).trim());
        if (method == null) {
            setErrorType(MyException.UNKNOWN_METHOD_NAME);
            return false;
        }
        try {
            // This try if the method contain argument
            if (matcher.group(2).trim().length() == ZERO){
                if (method.getVariables().size() != ZERO) {
                    setErrorType(MyException.NUMBER_OF_ARGUMENT);
                    return false;
                }
                return true;
            }
            String[] values = matcher.group(2).trim().split(Patterns.SYMBOL_PATTERN.getValue());
            // Check the number of the given argument
            // and how many parameter have been given to the method
            if (method.getVariables().size() != values.length) {
                setErrorType(MyException.NUMBER_OF_ARGUMENT);
                return false;
            }
            int i = 0;
            // Iteration over the given parameter and check there type with
            // The method argument type
            for (Variable entry : method.getVariableList()) {
                // That's mean the parameter is not number boolean string or char
                if (!valueMatchType(values[i].trim(), entry.getVariableType())) {
                    // That's mean the parameter is variable
                    Variable variable = getVariableFromTree(values[i].trim(), scope);
                    if (variable == null) {     // Unknown Parameter
                        setErrorType(MyException.PARAMETER_DOESNT_EXIST);
                        return false;
                    }
                    // This check if can make a casting
                    if (!canMakeCasting(entry.getVariableType(), variable)) {
                        setErrorType(MyException.VALUE_ERROR);
                        return false;
                    }
                }
                i++;
            }
            // That's mean theres not argument in the method
        } catch (Exception e) {
            if (method.getVariables().size() != ZERO) {
                setErrorType(MyException.NUMBER_OF_ARGUMENT);
                return false;
            }
        }
        return true;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////Set variable/////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method set the variable info and create it
     *
     * @param name          name
     * @param type          type
     * @param isFinal       is it final
     * @param isInitialized is it initialized
     * @return return new variable
     */
    private static Variable setVariable(String name, String type, boolean isFinal, boolean isInitialized) {
        Variable variable = new Variable();
        variable.setFinal(isFinal);
        variable.setVariableName(name);
        variable.setVariableType(type);
        variable.setInitialized(isInitialized);
        return variable;
    }


    /**
     * This method contain how many (,) in the line
     *
     * @param str line
     * @return integer
     */
    private static int countChar(String str) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ',')
                count++;
        }
        return count;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////Set Error/////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method to set the error
     *
     * @param errorType type
     */
    private static void setErrorType(String errorType) {
        Checker.errorType = errorType;
    }

    /**
     * @return Error type
     */
    public static String getErrorType() {
        return errorType;
    }

}
