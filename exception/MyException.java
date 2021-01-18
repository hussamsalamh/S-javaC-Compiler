package oop.ex6.main.exception;


/**
 * This class for exception
 * This class contain all the error msg for the program
 */
public class MyException extends Exception {

    public static final String UNKNOWN_LINE = "Error unknown line, check line ";
    public static final String SYNTAX_ERROR = "Error in this line ";
    public static final String ARGUMENT_ERROR = "Error in argument in line ";
    public static final String VALUE_ERROR = "The given value doesnt match the type , check line ";
    public static final String DOUBLE_DECLARE = "Error this element have been declare , Error in line ";
    public static final String BRACKET_ERROR = "You forget to put bracket , check the code again";
    public static final String FINAL_DECLARE = "Final variable cant be declare check line ";
    public static final String NO_RETURN = "No return in the method or you forget to put <;>  in line ";
    public static final String PARAMETER_HAS_NOT_INITIALIZED = "This parameter hasn't " +
            "been initialize ,check line ";
    public static final String PARAMETER_DOESNT_EXIST = "Parameter doesnt exist , check line ";
    public static final String PARAMETER_NAME_ERROR = "The parameter name is wrong , in line ";
    public static final String VARIABLE_VALUE_ERROR = "Error in value <Doesnt match type or " +
            "the variable doesnt declare in line ";
    public static final String UNKNOWN_METHOD_NAME = "Error, Unknown method name in line ";
    public static final String NUMBER_OF_ARGUMENT = "Error, Number of arguments are wrong , line ";
    public static final String IF_WHILE_ERROR = "Error , the condition must be " +
            "<boolean,int,double>, check line ";

    public MyException(String error) throws Exception {
        throw new Exception(error);
    }

    public MyException(int line, String error) throws Exception {
        throw new Exception(error+line);
    }


}
