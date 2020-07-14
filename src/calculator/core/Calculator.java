package calculator.core;

import calculator.UtilClass;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    public String getAnswerNumberPercentOfExpression(String beforePercent, String numberOfPercent){
        this.setExpression(beforePercent);
        String amountBP = getAnswerExpression();

        amountBP = UtilClass.cloneString(changeCommaOnPoint(amountBP));
        numberOfPercent = UtilClass.cloneString(changeCommaOnPoint(numberOfPercent));


        if(numberOfPercent.contains("pow") || numberOfPercent.contains("√") || numberOfPercent.contains("1⁒")){
            numberOfPercent = UtilClass.cloneString(removeBracketsWithOneDigit(numberOfPercent));
            numberOfPercent = UtilClass.cloneString(completionsSpecSymbols(numberOfPercent));
        }


        double numberPercentOfAmount = (Double.parseDouble(amountBP) * Double.parseDouble(numberOfPercent)) / 100;
        String result = clearZerosAfterPoint(String.valueOf(numberPercentOfAmount));
        result = editLengthExpressionAfterPoint(result);

        result = changePointOnComma(result);
        return result;
    }

    public String getAnswerExpression(){
        String editExpression = UtilClass.cloneString(changeCommaOnPoint());
        editExpression = UtilClass.cloneString(UtilClass.editMinuses(editExpression));
        boolean isStop = false;
        editExpression = UtilClass.cloneString(removeBracketsWithOneDigit(editExpression));

        int count = 0;

        while (!isStop) {
            if(hasMoreSymbols(editExpression) && hasMoreNumbers(editExpression)) {
                System.out.println((count++) + " Work with editExpression: " + editExpression);

                editExpression = UtilClass.cloneString(completionsSpecSymbols(editExpression));
                editExpression = UtilClass.cloneString(removeBracketsWithOneDigit(editExpression));
                System.out.println("editExpression after specSymbols and brackets: " + editExpression);

                editExpression = UtilClass.editMinuses(editExpression);
                System.out.println("after editMinus: " + editExpression);

                editExpression = UtilClass.cloneString(completionsMultiplyAndDivided(editExpression));
                editExpression = UtilClass.cloneString(removeBracketsWithOneDigit(editExpression));
                System.out.println();
                System.out.println("editExpression after multi.Divided and brackets: " + editExpression);

                editExpression = UtilClass.editMinuses(editExpression);
                System.out.println("after editMinus: " + editExpression);
//                System.exit(1);

                editExpression = UtilClass.cloneString(completionsPlusAndMinus(editExpression));
                editExpression = UtilClass.cloneString(removeBracketsWithOneDigit(editExpression));
                System.out.println("After \"+-\" " + count + " times. editExpression = " + editExpression +"\r\n");

            }else {
                isStop = true;
            }
        }

        editExpression = changePointOnComma(editExpression);
        return editExpression;
    }

    private boolean hasMoreNumbers(String expression){
        String clone = expression.replaceAll("\\.", "");
        Pattern pattern = Pattern.compile("[+\\-×/]?-?\\d+");
        Matcher matcher = pattern.matcher(clone);
        List<String> numbers = new ArrayList<>();

        while (matcher.find()){
            int start = matcher.start(), end = matcher.end();
            if(clone.charAt(start) != '-' && !Character.isDigit(clone.charAt(start))){
                start += 1;
            }
            numbers.add(clone.substring(start, end));
        }

        System.out.println(numbers.toString());

        if(numbers.size() > 1) {
            return true;
        }

        if(numbers.size() == 1 && containsIn(expression, "pow", "√", "1⁒")){
            return true;
        }

        return false;
    }

    private String changeCommaOnPoint(){
        String editExpression = UtilClass.cloneString(expression.replaceAll(",", "."));
        return editExpression;
    }
    private String changeCommaOnPoint(String expression){
        String editExpression = UtilClass.cloneString(expression.replaceAll(",", "."));
        return editExpression;
    }

    private String changePointOnComma(String result){
        String editExpression = UtilClass.cloneString(result.replaceAll("\\.", ","));
        return editExpression;
    }

    private String completionsMultiplyAndDivided(String expression){
        boolean isStop = false;
        String answer = UtilClass.cloneString(expression);

        while (!isStop){
            if(expression.contains("/") || expression.contains("×")) {
                int idSymbol = getIdNextMultiplyOrDivided(expression);
                System.out.println();
                System.out.println("C. */: idSymbol: " + idSymbol + " char = " + expression.charAt(idSymbol));
//                System.exit(1);
                String partExpression = getCutExpression(idSymbol, expression);
                System.out.println("completionsMultiplyAndDivided partExpression: " + partExpression);
                String answerPart = getResultMultiplyOrDivided(partExpression);
                System.out.println("completionsMultiplyAndDivided answerPart: " + answerPart);
//                System.exit(1);
                if (answerPart.equals(partExpression)) {
                    isStop = true;
                } else {
                    expression = replace(partExpression, answerPart, expression);
                }

            }else {
                isStop = true;
                answer = UtilClass.cloneString(expression);
            }
        }

        return answer;
    }

    private String getResultMultiplyOrDivided(String expression){
        String[] digits = expression.split("[/×]");

        if(!(containsIn(digits[0], "pow", "√", "1⁒", ")", "(") ||
                containsIn(digits[1], "pow", "√", "1⁒", ")", "(")) &&
                digits[0].length() != 0 && digits[1].length() != 0) {
            double d1 = Double.parseDouble(digits[0]);
            double d2 = Double.parseDouble(digits[1]);
            String result;

            if (expression.contains("×")) {
                double answer = d1 * d2;
                result = getResultFormE(answer);
            } else {
                double answer = d1 / d2;
                result = getResultFormE(answer);
            }

            result = editLengthExpressionAfterPoint(result);
            String finalResult = clearZerosAfterPoint(result);

            return finalResult;
        }

        return expression;
    }

    private String completionsPlusAndMinus(String expression){
        boolean isStop = false;
        String answer = UtilClass.cloneString(expression);
            while (!isStop) {
                if (containsPlusOrMinus(expression)) {
                    int idSymbol = getIdNextPlusOrMinus(expression);
                    System.out.println("id next symbol: " + idSymbol);
                    String partExpression = getCutExpression(idSymbol, expression);
                    String answerPart = getResultPlusAndMinus(partExpression, idSymbol);
                    System.out.println("partExpression: " + partExpression + "; answerPart: " + answerPart);
                    if (answerPart.equals(partExpression)) {
                        return partExpression;
                    } else {
                        expression = replace(partExpression, answerPart, expression);
//                        System.out.println("expression after replace: " + expression);
                        expression = UtilClass.cloneString(removeBracketsWithOneDigit(expression));
                    }

                } else {
                    isStop = true;
                    answer = UtilClass.cloneString(expression);
                }
            }

        return answer;
    }

    private String getResultPlusAndMinus(String expression, int idSymbol){
        String[] digits;
        System.out.println("expression in result+- " + expression);
        if(!expression.contains("E")){
            digits = getNumbers(expression, idSymbol);
        }else{
            digits = getNumbersWithE(expression);
        }

        boolean hasE = false;
        if(digits[0].contains("E") || digits[1].contains("E")){
            hasE = true;
        }

        System.out.println("hasE = " + hasE);
        System.out.println("digits: " + Arrays.toString(digits));
        if(!(containsIn(digits[0], "pow", "√", "1⁒", ")", "(")
                || containsIn(digits[1], "pow", "√", "1⁒", ")", "(")) &&
                digits[0].length() != 0 && digits[1].length() != 0) {
            double d1 = Double.parseDouble(digits[0]);
            double d2 = Double.parseDouble(digits[1]);
            String result;

            if (expression.contains("+")) {
                double answer = d1 + d2;
                System.out.println("answer + =" + answer);
                result = hasE ? getResultFormE(answer) : getResultFromLargeDigit(answer);
            } else {
                double answer = d1 - d2;
                System.out.println("answer - =" + answer);
                result = hasE ? getResultFormE(answer) : getResultFromLargeDigit(answer);
            }

            result = editLengthExpressionAfterPoint(result);
            String finalResult = clearZerosAfterPoint(result);

            System.out.println("finalResult get+- " + finalResult + "\r\n");
            return finalResult;
        }

        return expression;
    }
    private String[] getNumbersWithE(String expression){
        Pattern pattern = Pattern.compile("\\d+\\.?\\d+E[\\-+]\\d+");
        Matcher matcher = pattern.matcher(expression);
        List<String> digitsE = new ArrayList<>();
        String[] numbers = new String[2];
        int[] lastIdsE = new int[2];

        while(matcher.find()){
            lastIdsE[0] = matcher.start();
            lastIdsE[1] = matcher.end();
            digitsE.add(expression.substring(lastIdsE[0], lastIdsE[1]));
        }

        if(digitsE.size() != 2){
            numbers[0] = digitsE.get(0);

            if(lastIdsE[0] == 0){
                StringBuilder digit = new StringBuilder();
                for(int i = expression.length()-1; i > lastIdsE[1]; i--){
                    digit.append(expression.charAt(i));
                }
                digit.reverse();
                numbers[1] = digit.toString();
            }

        }else {
            for(int i = 0; i < digitsE.size(); i++){
                numbers[i] = digitsE.get(i);
            }
        }

        return numbers;
    }
    private String[] getNumbers(String expression, int idSymbol){
        String[] digits = new String[2];
        digits[0] = expression.substring(0, idSymbol);
        digits[1] = expression.substring(idSymbol+1);

        return digits;
    }

    private String completionsSpecSymbols(String expression){
        boolean isStop = false;
        String answer = UtilClass.cloneString(expression);
        while (!isStop){
            if(containsSpecSymbols(expression)) {
                int idSymbol = getIdNextSpecSymbol(expression);
                System.out.println("\r\nidSymbol : " + idSymbol);
                String partExpression = getCutExpressionWithSpecSymbol(idSymbol, expression);
                System.out.println("partExpression: " + partExpression);
                String answerPart = getResultSpecSymbols(partExpression);
                System.out.println("answerPart: " + answerPart);
//                System.exit(1);
                expression = replace(partExpression, answerPart, expression);

                expression = UtilClass.cloneString(removeBracketsWithOneDigit(expression));
                System.out.println("edit expression "+ expression);
//                System.exit(1);
            }else {
                isStop = true;
                answer = UtilClass.cloneString(expression);
            }
        }

        System.out.println("result SpecSymbols: " + answer);


        return answer;
    }

    private String getResultSpecSymbols(String expression){
        double digit;
        String answer = "";
        String result = "";
        if(expression.contains("pow")){
            digit = Double.parseDouble(expression.substring(3));
            answer = getResultFromPow(digit);

        } else if (expression.contains("√")) {
            digit = Double.parseDouble(expression.substring(1));
            answer = String.valueOf(Math.sqrt(digit));

        }else if (expression.contains("1⁒")){
            digit = Double.parseDouble(expression.substring(2));
            answer = String.valueOf(1/digit);
        }
        result = editLengthExpressionAfterPoint(String.valueOf(answer));
        result = UtilClass.cloneString(clearZerosAfterPoint(result));
        result = UtilClass.cloneString(editLengthExpressionAfterPoint(result));

        return result;
    }

    private String getResultFromPow(double digit){
        String answer = String.valueOf(Math.pow(digit, 2));
        if (answer.contains("E")) {
            BigDecimal bigDecimal = new BigDecimal(digit, new MathContext(2, RoundingMode.HALF_EVEN));
            answer = bigDecimal.toString();
        }
        return answer;
    }
    private String getResultFormE(double digit){
        String answer = String.valueOf(digit);
        if (answer.contains("E")) {
            BigDecimal bigDecimal = new BigDecimal(digit, new MathContext(12, RoundingMode.HALF_EVEN));
            answer = bigDecimal.toString();
        }
        return answer;
    }
    private String getResultFromLargeDigit(double digit){
        BigDecimal bigDecimal = new BigDecimal(digit);
        return bigDecimal.round(new MathContext(6)).toString();
    }

    private String removeBracketsWithOneDigit(String expression){
        Pattern pattern = Pattern.compile("\\([0-9.\\-]+\\)");
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

        return UtilClass.cloneString(expression);
    }

    private String replace(String oldE, String newE, String line){
        return UtilClass.cloneString(line.replace(oldE, newE));
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

            expression = UtilClass.cloneString(expression.substring(0, toCut));
        }
        return expression;
    }

    private String editLengthExpressionAfterPoint(String result){
        int idPoint = result.indexOf(".");
        if(idPoint != -1) {
            int cutId = result.length();

            for(int i = result.length()-1; i >= idPoint; i--){
                char chr = result.charAt(i);
                if(chr != '0'){
                    cutId = i+1;
                    i = idPoint;
                }
            }

            return UtilClass.cloneString(result.substring(0, cutId));
        }else {
            return UtilClass.cloneString(result);
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
        String cloneExpression = UtilClass.cloneString(expression);
        int plus = 0;
        int id;
        while (true) {
            int idPlus = cloneExpression.indexOf("+");
            int idMinus = cloneExpression.indexOf("-");

            id = Math.min(idPlus, idMinus);
            if (id <= 0) id = Math.max(idPlus, idMinus);

            int preChar = id+plus - 1;
            int nextChar = id+plus + 1;

            if (id != 0) {
                if(Character.isDigit(expression.charAt(nextChar)) && Character.isDigit(expression.charAt(preChar))) {
                    int a = id + plus;
                    return a;
                }
            }else{
                plus = plus + 1 + id;
                cloneExpression = UtilClass.cloneString(cloneExpression.substring(id+1));
            }
        }
    }

    private int getIdNextSpecSymbol(String expression){
        int idPow = -1, idRoot = -1, idOD = -1;
        Pattern pattern = Pattern.compile("pow[0-9.\\-]+");
        Matcher matcher = pattern.matcher(expression);

        if(matcher.find()) {
            idPow = matcher.start();
        }

        pattern = Pattern.compile("√[0-9.]+");
        matcher = pattern.matcher(expression);

        if(matcher.find()) {
            idRoot = matcher.start();
        }

        pattern = Pattern.compile("1⁒[0-9.]+");
        matcher = pattern.matcher(expression);

        if(matcher.find()) {
            idOD = matcher.start();
        }

        int result;

        if(idPow != -1 && idRoot != -1) result = Math.min(idPow, idRoot);
        else result = Math.max(idPow, idRoot);


        if(idOD != -1 && result != -1) result = Math.min(result, idOD);
        else result = Math.max(idOD, result);

        return result;
    }

    private String getCutExpression(int idSymbol, String expression){
        if(!(expression.substring(0, idSymbol).contains("E") || expression.substring(idSymbol).contains("E"))) {
            StringBuilder firstDigit = new StringBuilder();
            int startId = idSymbol-1;
            boolean wasMinus = false;

            for(; startId >= 0; startId--){
                char chr = expression.charAt(startId);
                System.out.print(chr);
                if(!isEqualSymbol(chr, '+', '/', '×', '(', ')')){
                    if(chr == '-'){
                        if(startId == 0){
                            firstDigit.append(chr);
                        }else if(!wasMinus){
                                wasMinus = true;
                        }
                    }

                    if((Character.isDigit(chr) || chr == '.') && !wasMinus) {
                        firstDigit.append(chr);
                    }else{
                        startId = 0;
                    }
                }else{
                    if(wasMinus && chr != ')'){
                        firstDigit.append("-");
                    }else {
                        startId = 0;
                    }
                }
            }

            System.out.println("\r\nfirstDigit: " + firstDigit);

            startId = idSymbol+1;
            wasMinus = false;
            StringBuilder secondDigit = new StringBuilder();

            for (; startId < expression.length(); startId++) {
                char chr = expression.charAt(startId);
                if(!isEqualSymbol(chr, '+', '/', '×', '(', ')')){
                    if(chr == '-'){
                        if(!wasMinus && startId == idSymbol + 1){
                            wasMinus = true;
                            secondDigit.append(chr);
                        }else{
                            startId = expression.length();
                        }
                    }else{
                        secondDigit.append(chr);
                    }
                }else{
                    startId = expression.length();
                }
            }

            System.out.println("secondDigit: " + secondDigit);
//            System.exit(1);
            if (UtilClass.isDigit(firstDigit.toString()) && UtilClass.isDigit(secondDigit.toString())) {
                String answer = firstDigit.reverse().toString() + expression.charAt(idSymbol) + secondDigit.toString();
                return answer;
            }

            return expression;
        }else {
            String[] numbers = getNumbersWithE(expression);
            String answer = numbers[0] + expression.charAt(idSymbol) +numbers[1];
            return answer;
        }
    }

    private String getCutExpressionWithSpecSymbol(int idSymbol, String expression){
        int lengthSpecS = expression.contains("pow") ? 3 : expression.contains("1⁒") ? 2 : 1;
        int startId = idSymbol;
        StringBuilder result = new StringBuilder();
//        boolean hasPowNegativeDigit = hasNegativeDigitInPow(expression, idSymbol);
        boolean wasMinus = false;

        for (; idSymbol < expression.length(); idSymbol++) {
            char chr = expression.charAt(idSymbol);
            if(!isEqualSymbol(chr, '+', '/', '×', '(', ')')){
                if(chr == '-'){
                    if(!wasMinus && startId + lengthSpecS == idSymbol){
                        wasMinus = true;
                        result.append(chr);
                    }else{
                        idSymbol = expression.length();
                    }
                }else{
                    result.append(chr);
                }
            }else{
                idSymbol = expression.length();
            }
        }
        System.out.println("getCutExpressionWithSpecSymbol result: " + result.toString());
//        System.exit(1);
        return result.toString();
    }

    private boolean hasNegativeDigitInPow(String expression, int from){
        Pattern pattern = Pattern.compile("pow[0-9.\\-]+");
        Matcher matcher = pattern.matcher(expression.substring(from));

        if(matcher.find()) {
            return true;
        }
        return false;
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
        Pattern patternOne = Pattern.compile("[0-9.]+\\+[0-9.]+");
        Matcher matcher = patternOne.matcher(line);

        if(matcher.find()) return true;

        patternOne = Pattern.compile("[0-9.]+-[0-9.]+");
        matcher = patternOne.matcher(line);

        if(matcher.find()) return true;

        return false;
    }

    private boolean containsSpecSymbols(String line){
        Pattern patternOne = Pattern.compile("pow[0-9.\\-]+");
        Matcher matcher = patternOne.matcher(line);

        if(matcher.find()){
            return true;
        }

        patternOne = Pattern.compile("√[0-9.]+");
        matcher = patternOne.matcher(line);

        if(matcher.find()) {
            return true;
        }

        patternOne = Pattern.compile("1⁒[0-9.]+");
        matcher = patternOne.matcher(line);

        if(matcher.find()) {
            return true;
        }

        return false;
    }

    public Calculator(){}

    private String expression;
    private boolean hasBrackets;
    private boolean hasSpecSymbols;
    private String[] symbols = {"+", "/", "-", "×", "pow", "√", "1⁒"};

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
