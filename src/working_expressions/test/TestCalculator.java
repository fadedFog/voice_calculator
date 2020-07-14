package working_expressions.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCalculator {
        // On Mind
    // "," == "."
    /* Priority (by Order Calculators):
    *         1] Brackets
    *         2] Spec-Symbols
    *         3] Divided And Multiply
    *         4] Plus And Minus
    **/

    public String getAnswerNumberPercentOfExpression(String beforePercent, String numberOfPercent){
        // (digit * nPercent) / 100
        this.setExpression(beforePercent);
        String amountBP = getAnswerExpression();

        double numberPercentOfAmount = (Double.parseDouble(amountBP) * Double.parseDouble(numberOfPercent)) / 100;
        String result = clearZerosAfterPoint(String.valueOf(numberPercentOfAmount));
        result = editLengthExpressionAfterPoint(result);

        return result;
    }

    public String getAnswerExpression(){
        //System.out.println("\r\nOrigin Expression: " + expression);
        String editExpression = TestUtilClass.cloneString(changeCommaOnPoint());
        editExpression = TestUtilClass.cloneString(TestUtilClass.editMinuses(editExpression));

        boolean isStop = false;
        editExpression = TestUtilClass.cloneString(removeBracketsWithOneDigit(editExpression));

        //System.out.println("EditExpression: " + editExpression);

        while (!isStop) {
            if(hasMoreSymbols(editExpression)) {
                editExpression = TestUtilClass.cloneString(completionsMultiplyAndDivided(editExpression));
                editExpression = TestUtilClass.cloneString(removeBracketsWithOneDigit(editExpression));

                editExpression = TestUtilClass.cloneString(completionsPlusAndMinus(editExpression));
                editExpression = TestUtilClass.cloneString(removeBracketsWithOneDigit(editExpression));

                editExpression = TestUtilClass.cloneString(completionsSpecSymbols(editExpression));
                editExpression = TestUtilClass.cloneString(removeBracketsWithOneDigit(editExpression));

            }else {
                isStop = true;
            }
        }

        ////System.out.println("ANSWER: " + editExpression);

        return editExpression;
    }

    private String changeCommaOnPoint(){
        String editExpression = TestUtilClass.cloneString(expression.replaceAll(",", "."));
        return editExpression;
    }

    private String completionsMultiplyAndDivided(String expression){
        boolean isStop = false;
        String answer = TestUtilClass.cloneString(expression);

        while (!isStop){
            if(expression.contains("/") || expression.contains("×")) {
                int idSymbol = getIdNextMultiplyOrDivided(expression);
//                ////System.out.println("completionsMultiplyAndDivided idSymbol " + idSymbol);
                String partExpression = getCutExpression(idSymbol, expression);
                String answerPart = getResultMultiplyOrDivided(partExpression);
                if(answerPart.equals(partExpression)) {
                    isStop = true;
                }else{
                    expression = replace(partExpression, answerPart, expression);
                }
//                ////System.out.println(partExpression + " = " + answerPart);
//                ////System.out.println(expression);

            }else {
                isStop = true;
                answer = TestUtilClass.cloneString(expression);
            }
        }

        return answer;
    }

    private String getResultMultiplyOrDivided(String expression){
        String[] digits = expression.split("[/×]");

        if(!(containsIn(digits[0], "pow", "√", "1⁒", ")", "(") ||
                containsIn(digits[1], "pow", "√", "1⁒", ")", "(")) &&
                digits[0].length() != 0 && digits[1].length() != 0) {
            //System.out.println("digits[0] " + digits[0]);
            double d1 = Double.parseDouble(digits[0]);
            double d2 = Double.parseDouble(digits[1]);
            String result;

            if (expression.contains("×")) {
                double answer = d1 * d2;
                result = String.valueOf(answer);
            } else {
                double answer = d1 / d2;
                result = String.valueOf(answer);
            }

            result = editLengthExpressionAfterPoint(result);
            String finalResult = clearZerosAfterPoint(result);

            return finalResult;
        }

        return expression;
    }

    private String completionsPlusAndMinus(String expression){
        boolean isStop = false;
        String answer = TestUtilClass.cloneString(expression);

        while (!isStop){
            if(containsPlusOrMinus(expression)) {
                int idSymbol = getIdNextPlusOrMinus(expression);
//                System.out.println("completionsMultiplyAndDivided idSymbol " + idSymbol);
//                System.out.println("expression "  + expression);
                String partExpression = getCutExpression(idSymbol, expression);
//                System.out.println("partExpression " + partExpression);
                String answerPart = getResultPlusAndMinus(partExpression);
                if(answerPart.equals(partExpression)){
                    return partExpression;
                }else {
                    expression = replace(partExpression, answerPart, expression);
                    expression = TestUtilClass.cloneString(removeBracketsWithOneDigit(expression));
                }
//                ////System.out.println(partExpression + " = " + answerPart);
//                ////System.out.println(expression);

            }else {
                isStop = true;
//                System.err.println("Answer +- " + expression );
                answer = TestUtilClass.cloneString(expression);
            }
        }

        return answer;
    }

    private String getResultPlusAndMinus(String expression){
//        System.out.println("expression for digits: " + expression);

        String[] digits = expression.split("[+\\-]");
//        //System.out.println("digits[0]" + digits[0] + " digits[1]" + digits[1]);
        if(!(containsIn(digits[0], "pow", "√", "1⁒", ")", "(")
                || containsIn(digits[1], "pow", "√", "1⁒", ")", "(")) &&
                digits[0].length() != 0 && digits[1].length() != 0) {
            double d1 = Double.parseDouble(digits[0]);
            double d2 = Double.parseDouble(digits[1]);
            String result;

            if (expression.contains("+")) {
                double answer = d1 + d2;
                result = String.valueOf(answer);
            } else {
                double answer = d1 - d2;
                result = String.valueOf(answer);
            }

            result = editLengthExpressionAfterPoint(result);
            String finalResult = clearZerosAfterPoint(result);

            return finalResult;
        }

        return expression;
    }

    private String completionsSpecSymbols(String expression){
        boolean isStop = false;
        String answer = TestUtilClass.cloneString(expression);

        while (!isStop){
            if(containsSpecSymbols(expression)) {
                int idSymbol = getIdNextSpecSymbol(expression);
                ////System.out.println("completionsSpecSymbols idSymbol " + idSymbol);
                String partExpression = getCutExpressionWithSpecSymbol(idSymbol, expression);
                String answerPart = getResultSpecSymbols(partExpression);
                expression = replace(partExpression, answerPart, expression);

                expression = TestUtilClass.cloneString(removeBracketsWithOneDigit(expression));

                //System.out.println(partExpression + " = " + answerPart);
                ////System.out.println("Expression " + expression);
            }else {
                isStop = true;
                answer = TestUtilClass.cloneString(expression);
            }
        }

        return answer;
    }

    private String getResultSpecSymbols(String expression){
        double digit;
        double answer = 0;
        String result = "";
        if(expression.contains("pow")){
            digit = Double.parseDouble(expression.substring(3));
            answer = Math.pow(digit, 2);

        } else if (expression.contains("√")) {
            digit = Double.parseDouble(expression.substring(1));
            answer = Math.sqrt(digit);

        }else if (expression.contains("1⁒")){
            digit = Double.parseDouble(expression.substring(2));
            answer = 1/digit;
//            System.err.println(answer);
        }
        result = editLengthExpressionAfterPoint(String.valueOf(answer));
        result = TestUtilClass.cloneString(clearZerosAfterPoint(result));
        result = TestUtilClass.cloneString(editLengthExpressionAfterPoint(result));

        return result;
    }

    private String removeBracketsWithOneDigit(String expression){
        Pattern pattern = Pattern.compile("\\([0-9\\.]+\\)");
        Matcher matcher = pattern.matcher(expression);
        List<String> brackets = new ArrayList<>();

        while (matcher.find()){
            brackets.add(expression.substring(matcher.start(), matcher.end()));
        }

        if(brackets.size() > 0) {
            for (String element : brackets) {
                String digit = element.substring(1, element.length() - 1);
                expression = replace(element, digit, expression);
            }
        }

        return TestUtilClass.cloneString(expression);
    }

    private String replace(String oldE, String newE, String line){
        return TestUtilClass.cloneString(line.replace(oldE, newE));
    }

    private String clearZerosAfterPoint(String expression){
        if(expression.contains(".")) {
            int numberCut = 0;

            for (int i = expression.length()-1; i >= 0; i--){
                char chr = expression.charAt(i);
                if(chr == '0' || chr == '.'){
                    numberCut +=1;
                    if(chr == '.'){
                        i = 0;
                    }
                }else{
                    i = 0;
                }
            }

            int toCut = expression.length() - numberCut;

            expression = TestUtilClass.cloneString(expression.substring(0, toCut));
        }
//        ////System.out.println(expression);
        return expression;
    }

    private String editLengthExpressionAfterPoint(String result){
        int idPoint = result.indexOf(".");
        if(idPoint != -1) {
            int lengthBeforePoint = result.substring(0, idPoint).length();
            int lengthAfterPoint = result.substring(idPoint).length();

            int cutId = lengthBeforePoint;
//            System.err.println("cutId " + cutId);
            if (lengthAfterPoint > 4) cutId += 5;
            else return result;

            return TestUtilClass.cloneString(result.substring(0, cutId));
        }else {
            return TestUtilClass.cloneString(result);
        }
    }

    private int getIdNextMultiplyOrDivided(String expression){
        int idMultiply = expression.indexOf("×");
        int idDivided = expression.indexOf("/");

        int id = Math.min(idMultiply, idDivided);

        if(id <= 0) id = Math.max(idMultiply, idDivided);

        return id;
    }

    private int getIdNextPlusOrMinus(String expression){
//        System.out.println("\r\nExpression " + expression);

        String cloneExpression = TestUtilClass.cloneString(expression);
        int plus = 0;
        int id;
        while (true) {
            int idPlus = cloneExpression.indexOf("+");
            int idMinus = cloneExpression.indexOf("-");

            id = Math.min(idPlus, idMinus);
            if (id <= 0) id = Math.max(idPlus, idMinus);
//            System.out.println("id before: " + (id+plus));
//            System.out.println(Character.isDigit(expression.charAt(id+plus + 1))  + " && " + Character.isDigit(expression.charAt(id+plus - 1)));

            if (Character.isDigit(expression.charAt(id+plus + 1)) && Character.isDigit(expression.charAt(id+plus - 1))) {
                int a = id+plus;
//                System.out.println("id " + expression.charAt(id+plus) + " id+plus " + a);
                return id+plus;
            }else{
                plus = plus + 1 + id;
                cloneExpression = TestUtilClass.cloneString(cloneExpression.substring(id+1));
            }
        }
    }

    private int getIdNextSpecSymbol(String expression){
        int idPow = -1, idRoot = -1, idOD = -1;
        Pattern pattern = Pattern.compile("pow[0-9\\.]+");
        Matcher matcher = pattern.matcher(expression);

        if(matcher.find()) {
//            ////System.out.println("pow " + expression.substring(matcher.start(), matcher.end()));
            idPow = matcher.start();
            ////System.out.println("idPow " + idPow);
        }

        pattern = Pattern.compile("√[0-9\\.]+");
        matcher = pattern.matcher(expression);

        if(matcher.find()) {
            idRoot = matcher.start();
            ////System.out.println("idRoot " + idRoot);
        }

        pattern = Pattern.compile("1⁒[0-9\\.]+");
        matcher = pattern.matcher(expression);

        if(matcher.find()) {
            idOD = matcher.start();
            ////System.out.println("idRoot " + idOD);
        }

//        System.err.println("idPow, idRoot, idOD " + idPow + " " + idRoot + " " + idOD);

        int result;

        if(idPow != -1 && idRoot != -1) result = Math.min(idPow, idRoot);
        else result = Math.max(idPow, idRoot);


        if(idOD != -1 && result != -1) result = Math.min(result, idOD);
        else result = Math.max(idOD, result);

//        System.err.println("result: " + result);

        return result;
    }

    private String getCutExpression(int idSymbol, String expression){
        //System.out.println();
        StringBuilder firstDigit = new StringBuilder();
//        System.out.println("idSymbol-first " + idSymbol + " symbol " + expression.charAt(idSymbol));
        int startId = idSymbol-1;

        for(; startId >= 0; startId--){
            if(!isEqualSymbol(expression.charAt(startId), '-', '+', '/', '×', '(', ')')){
                firstDigit.append(expression.charAt(startId));
            }else {
                startId = 0;
            }
        }

//        System.out.println("First Digit: " + firstDigit.toString() + " isDigit " + isDigit(firstDigit.toString()));

        startId = idSymbol + 1;
        StringBuilder secondDigit = new StringBuilder();

        for(; startId < expression.length(); startId++){
            if(!isEqualSymbol(expression.charAt(startId), '-', '+', '/', '×', '(', ')')){
                secondDigit.append(expression.charAt(startId));
            }else {
                startId = expression.length()-1;
            }
        }

//        System.out.println("Second Digit: " + secondDigit.toString() + " isDigit " + isDigit(secondDigit.toString()));
//        System.out.println(firstDigit.reverse().toString() + expression.charAt(idSymbol) + secondDigit.toString());
//        firstDigit.reverse();
        if(isDigit(firstDigit.toString()) && isDigit(secondDigit.toString())){
            String answer = firstDigit.reverse().toString() + expression.charAt(idSymbol) + secondDigit.toString();
//            System.out.println("answer " + answer);
            return answer;
        }
        //System.out.println("expression GCE " + expression);
        //System.out.println();

        return expression;
    }

    private String getCutExpressionWithSpecSymbol(int idSymbol, String expression){
        StringBuilder result = new StringBuilder();
//        //System.out.println(idSymbol);
        for(; idSymbol < expression.length(); idSymbol++){
            if(!isEqualSymbol(expression.charAt(idSymbol), '-', '+', '/', '×', '(', ')')){
                result.append(expression.charAt(idSymbol));
            }else{
                idSymbol = expression.length();
            }
        }
//        getIdNextPlusOrMinus("result GCEWSS" + result.toString());
        return result.toString();
    }

    private boolean isEqualSymbol(char element, char ... symbols){
        for(char symbol: symbols){
            if(symbol == element) return true;
        }

        return false;
    }

    private boolean hasMoreSymbols(String expression){
        for(String symbol: symbols){
            if(expression.contains(symbol)) return true;
        }
        return false;
    }

    private boolean containsIn(String line, String ... elements){
        for(String element: elements){
            if(line.contains(element)){
                return true;
            }
        }
        return false;
    }

    private boolean containsPlusOrMinus(String line){
        Pattern patternOne = Pattern.compile("[0-9\\.]+\\+[0-9\\.]+");
        Matcher matcher = patternOne.matcher(line);

        if(matcher.find()) return true;

        patternOne = Pattern.compile("[0-9\\.]+-[0-9\\.]+");
        matcher = patternOne.matcher(line);

        if(matcher.find()) return true;

        return false;
    }

    private boolean containsSpecSymbols(String line){
//        System.out.println("line " + line);
        Pattern patternOne = Pattern.compile("pow[0123456789.]+");
        Matcher matcher = patternOne.matcher(line);

        if(matcher.find()){
//            System.out.println("Pow true");
            return true;
        }

        patternOne = Pattern.compile("√[0-9.]+");
        matcher = patternOne.matcher(line);

        if(matcher.find()) {
//            System.out.println("√ true");
            return true;
        }

        patternOne = Pattern.compile("1⁒[0-9.]+");
        matcher = patternOne.matcher(line);

        if(matcher.find()) {
//            System.out.println("1⁒ true");
            return true;
        }

//        System.err.println(false);
        return false;
    }

    private boolean isDigit(String line){
        for(char element: line.toCharArray()){
            if(!Character.isDigit(element) && element != '.'){
                return false;
            }
        }
        return true;
    }

    public TestCalculator(String expression, boolean hasBrackets, boolean hasSpecSymbols) {
        this.expression = expression;
        this.hasBrackets = hasBrackets;
        this.hasSpecSymbols = hasSpecSymbols;
    }

    public TestCalculator(){}

    private String expression;
    private boolean hasBrackets;
    private boolean hasSpecSymbols;
    private String[] symbols = {"+", "/", "-", "×", "pow(", "√(", "1⁒("};

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setHasBrackets(boolean hasBrackets) {
        this.hasBrackets = hasBrackets;
    }

    public void setHasSpecSymbols(boolean hasSpecSymbols) {
        this.hasSpecSymbols = hasSpecSymbols;
    }
}
