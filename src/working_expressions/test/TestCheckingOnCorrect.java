package working_expressions.test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCheckingOnCorrect {

    public boolean isCorrectExpression(String expression){
        //return false -> Message: "Mistakes in expression"
        //return false -> Answer from working calculator.

//        System.out.println("expression: " + expression);

        String editExpression = TestUtilClass.editMinuses(expression);
//        System.out.println("editExpression: " + editExpression);

        if(!hasContainsOnlyExpressionElements(editExpression)){
//            System.err.println("Mistake in hasContainsOnlyExpressionElements");
            return false;
        }

        if(hasChaosSymbols(editExpression)) {
//            System.err.println("Mistake in hasChaosSymbols");
            return false;
        }

        if(hasNumberDividedZero(editExpression)) {
//            System.err.println("Mistake in hasNumberDividedZero");
            return false;
        }

        if(!isCountCloseBracketsEqualsOpenBrackets(editExpression)) {
//            System.err.println("Mistake in isCountCloseBracketsEqualsOpenBrackets");
            return false;
        }

        if(hasEmptyBrackets(expression)){
            return false;
        }

        return true;
    }

    private boolean hasEmptyBrackets(String expression){
        return expression.contains("()");
    }

    private boolean hasContainsOnlyExpressionElements(String expression){
        boolean hasSpecSymbol = hasExpressionSpecSymbols(expression);

        for(int i = 0; i < expression.length(); i++){
            char element = expression.charAt(i);

            if(!Character.isDigit(element)){
                if(!isSimplySymbol(String.valueOf(element))){

                    if(isElementContainsInSpecSymbols(String.valueOf(element))){
                        if(!hasSpecSymbol){
//                            System.err.println("last-check element: " + element);
                            return false;
                        }
                    }else{
//                        System.err.println("first-check element: " + element);
                        return false;
                    }

                }
            }
        }

        return true;
    }

    private boolean hasNumberDividedZero(String expression){
        if(expression.contains(specSymbols[2] + "0)")){
            return true;
        }

        if(expression.contains("/0")){
            Pattern patternOne = Pattern.compile("/0.");
            Matcher matcher = patternOne.matcher(expression);
            List<String> partsDividedZero = new ArrayList<>();

            while (matcher.find()){
                partsDividedZero.add(expression.substring(matcher.start(), matcher.end()));
            }
//            System.out.println(partsDividedZero.toString() + " " + partsDividedZero.size());
            if(partsDividedZero.size() == 0) return true;

            for(String part: partsDividedZero){
                if(!part.endsWith(",")) return true;
            }


        }

        return false;
    }

    private boolean isCountCloseBracketsEqualsOpenBrackets(String expression){
        List<Character> elementsExpression = new ArrayList<>();

        for(char element: expression.toCharArray())
            elementsExpression.add(element);

        int numberOpenBrackets = Collections.frequency(elementsExpression, '(');
        int numberCloseBrackets = Collections.frequency(elementsExpression, ')');
//        System.err.println("numberOpenBrackets: " + numberOpenBrackets + "\r\nnumberCloseBrackets: " + numberCloseBrackets);

        if(numberCloseBrackets == numberOpenBrackets){
            if(numberCloseBrackets == 0) return true;
            hasBrackets = true;

            return areBracketsInOrder(expression);
        }

//        System.err.println("Last ");
        return false;
    }

    private boolean areBracketsInOrder(String expression){
        List<Integer> idsOpenBrackets = new ArrayList<>();
        List<Integer> idsCloseBrackets = new ArrayList<>();
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) == '('){
                idsOpenBrackets.add(i);
            }else if(expression.charAt(i) == ')'){
                idsCloseBrackets.add(i);
            }
        }

        for(int i = 0; i < idsOpenBrackets.size(); i++){
            if(idsOpenBrackets.get(i) > idsCloseBrackets.get(i)){
//                System.err.println("OpenId > CloseId");
                return false;
            }
        }

        return true;
    }

    private boolean hasChaosSymbols(String expression){
        String firstExample = "+/";
        String secondExample = "+×";
        String thirdExample = "-/";
        String fourthExample = "-×";
        String fifthExample = "++";

        if(expression.contains(firstExample) || expression.contains(reverseString(firstExample))) return true;
        if(expression.contains(secondExample) || expression.contains(reverseString(secondExample))) return true;
        if(expression.contains(thirdExample) || expression.contains(reverseString(thirdExample))) return true;
        if(expression.contains(fourthExample) || expression.contains(reverseString(fourthExample))) return true;
        if(expression.contains(fifthExample)) return true;

        if(expression.contains("(") || expression.contains(")")) {
            if (!areSymbolsAboutBracketsCorrect(expression)) return true;
        }

        if(expression.contains(",")){
            if(!arePointsStateCorrect(expression)) return true;
        }

        return false;
    }

    private boolean areSymbolsAboutBracketsCorrect(String expression){
        Pattern patternOne = Pattern.compile("\\).");
        Matcher matcher = patternOne.matcher(expression);
        List<String> partsBrackets = new ArrayList<>();

        while (matcher.find()){
            partsBrackets.add(expression.substring(matcher.start(), matcher.end()));
        }

        for(String elements: partsBrackets){
            char lastChar = elements.charAt(elements.length()-1);
            if(Character.isDigit(lastChar) || lastChar == 's' || lastChar == '√' || lastChar == '(') return false;
        }


        patternOne = Pattern.compile(".\\(");
        matcher = patternOne.matcher(expression);
        partsBrackets.clear();

        while (matcher.find()){
            partsBrackets.add(expression.substring(matcher.start(), matcher.end()));
        }

        for(String elements: partsBrackets){
            char startChar = elements.charAt(0);
            if(Character.isDigit(startChar)) return false;
        }

        return true;
    }
    private boolean arePointsStateCorrect(String expression){
        Pattern patternOne = Pattern.compile(".,.");
        Matcher matcher = patternOne.matcher(expression);

        while(matcher.find()){
            String part = expression.substring(matcher.start(), matcher.end());
            char startChar = part.charAt(0);
            char endChar = part.charAt(part.length()-1);

            if(!(Character.isDigit(startChar) && Character.isDigit(endChar))) {
                return false;
            }
        }

        return true;
    }

    private boolean isSimplySymbol(String element){
        for(String symbol: simplySymbols){
            if(symbol.equals(element)) return true;
        }

        return false;
    }

    private boolean hasExpressionSpecSymbols(String expression){
        for(String symbol: specSymbols){
            if(expression.contains(symbol)) return true;
        }

        return false;
    }

    private boolean isElementContainsInSpecSymbols(String element){
        for(String symbol: specSymbols){
            if(symbol.contains(element)) {
                hasSpecSymbols = true;
                return true;
            }
        }
        return false;
    }

    private String reverseString(String line){
        StringBuilder reverseLine = new StringBuilder(line);
        return reverseLine.reverse().toString();
    }


    private boolean hasBrackets = false;
    private boolean hasSpecSymbols = false;

    private String[] simplySymbols = {"+", "/", "-", "×", ","};
    private String[] specSymbols = {"pow(", "√(", "1⁒(", ")"}; // end with ")"
    private String percent = "%";

    public boolean hasBrackets() {
        boolean clone = hasBrackets;
        hasBrackets = false;
        return clone;
    }

    public boolean hasSpecSymbols() {
        boolean clone = hasSpecSymbols;
        hasSpecSymbols = false;
        return clone;
    }
}



















