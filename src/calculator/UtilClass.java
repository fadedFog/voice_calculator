package calculator;

public class UtilClass {

    public static String cloneString(String expression){
        StringBuilder clone = new StringBuilder();
        for(int i = 0; i < expression.length(); i++){
            clone.append(expression.charAt(i));
        }
        return clone.toString();
    }

    public static String editMinuses(String expression){
        String editExpression = UtilClass.cloneString(expression);
        boolean isStop = false;

        while (!isStop) {
            if(editExpression.contains("--") || editExpression.contains("+-") || editExpression.contains("-+")) {
                editExpression = editExpression.replaceAll("--", "+");
                editExpression = editExpression.replaceAll("\\+-", "-");
                editExpression = editExpression.replaceAll("-\\+", "-");
            }else {
                isStop = true;
            }
        }

        return editExpression;
    }

    public static boolean isDigit(String line){
        for(char element: line.toCharArray()){
            if(!Character.isDigit(element) && element != '.' && element != ',' && element != '-'){
                return false;
            }
        }
        return true;
    }



}
