package working_expressions.test;

public class TestUtilClass {

    public static String cloneString(String expression){
        StringBuilder clone = new StringBuilder();
        for(int i = 0; i < expression.length(); i++){
            clone.append(expression.charAt(i));
        }
        return clone.toString();
    }

    public static String editMinuses(String expression){
        String editExpression = TestUtilClass.cloneString(expression);

        editExpression = editExpression.replaceAll("--", "+");
        editExpression = editExpression.replaceAll("\\+-", "-");
        editExpression = editExpression.replaceAll("-\\+", "-");
        return editExpression;
    }

}
