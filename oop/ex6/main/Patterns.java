package oop.ex6.main;


/**
 * This class contain only the pattern ( regex pattern ) for all the possibility line
 * can be in the code
 */

public enum Patterns {


    // Final variable regex
    FINAL_VARIABLE("^\\s*final\\s+.*$"),

    // Non-final variable pattern
    NON_FINAL_VARIABLE("^\\s*(int|double|char|String|boolean)\\s+(.+)\\s*[;]{1}\\s*$"),

    // Method line regex ( example : void nameMethod ( argument ) { )
    METHOD_LINE_PATTERN("^\\s*(void\\s+){1}\\s*([^0-9_]{1}[a-zA-Z0-9_]*)\\s*[(]{1}(.*)[)]{1}\\s*([{]{1}){1}\\s*$"),

    // if/ while line regex ( example : if ( condition) { / while ( condition ) { )
    IF_WHILE_PATTERN("^\\s*\\b(if|while){1}\\s*[(]{1}\\s*(.+)\\s*[)]{1}\\s*[{]{1}\\s*$"),

    // calling method regex ( example : methodName (arguments ) ; )
    CALLING_METHOD_PATTERN("^(\\s*.+){1}\\s*[(]{1}\\s*(.*)\\s*[)]\\s*[;]\\s*$"),

    // This for argument declaration in method
    ARGUMENT_DECLARATION_PATTERN("^\\s*(final\\s+)?((int|double|boolean|String|char)){1}\\s+(.+){1}\\s*$"),


    // variable name regex ( the allowed name )
    VARIABLE_NAME_PATTERN("^\\s*\\b[^0-9]{1}[a-zA-Z0-9_]+\\s*$"),

    //  regex for variable that contain one letter
    VARIABLE_NAME_PATTERN2("^\\s*[a-zA-Z]\\s*$"),

    // Return line regex
    RETURN_PATTERN("^\\s*(return){1}\\s*(;){1}\\s*$"),

    // void regex
    VOID_PATTERN("^\\s*(void){1}\\s+"),

    // This regex for closing scope " } "
    CLOSE_SCOPE_PATTERN("^\\s*[}]{1}\\s*$"),

    // This regex for opening scope " { "
    OPEN_SCOPE_PATTERN("^\\s*\\w(.+){1}[{]\\s*$"),

    // This regex split the condition
    SPLIT_CONDITION("([|]{2}|[&]{2})"),
    
    SYMBOL_PATTERN(","),

    //////////////////////////////////////////////////////
    //////////////////Allow types ///////////////////////

    // This regex for string type
    STRING_PATTERN("\\s*[\"]{1}\\s*.*[\"]{1}\\s*$"),

    // This regex for integer type
    INTEGER_PATTER("^\\s*([-]?[0-9]+){1}\\s*$"),

    // This regex for char type
    CHAR_PATTERN("^\\s*[']{1}.{1}[']{1}\\s*$"),

    // This regex for double type
    DOUBLE_PATTERN("^\\s*([-]?[0-9]+([.]{1}[0-9]+)?){1}\\s*$"),

    // This regex for boolean type
    BOOLEAN_PATTERN("^\\s*(true|false|([-]?[0-9]+(([.]{1}[0-9]+)?){1})|([-]?[0-9]+){1}){1}\\s*$"),

    // This regex for comment type
    COMMENT_PATTERN("^(//)");


    final static String INT_VALUE = "int";
    final static String STRING = "String";
    final static String CHAR = "char";
    final static String BOOLEAN = "boolean";
    final static String DOUBLE = "double";
    public final String PATTERN;

    Patterns(String pattern) {
        this.PATTERN = pattern;
    }

    /**
     * @return this method return the value that inside the brackets
     * COMMENT_PATTERN this method will return -- > "^(//)"
     */
    public String getValue() {
        return PATTERN;
    }

}
