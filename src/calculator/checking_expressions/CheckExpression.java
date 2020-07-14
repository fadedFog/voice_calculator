package calculator.checking_expressions;


import calculator.UtilClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckExpression {

    public boolean isCorrectExpression(String expression){
        String editExpression = UtilClass.editMinuses(expression);

        if(!hasContainsOnlyExpressionElements(editExpression)){
            System.out.println("tik1");
            typeException = TypeException.INCORRECT_DATA;
            return false;
        }

        if(hasChaosSymbols(editExpression)) {
//            System.out.println("tik2");
            typeException = TypeException.INCORRECT_DATA;
            return false;
        }

        if(hasNumberDividedZero(editExpression)) {
//            System.out.println("tik3");
            typeException = TypeException.DIVIDED_ON_ZERO;
            return false;
        }

        if(!isCountCloseBracketsEqualsOpenBrackets(editExpression)) {
//            System.out.println("tik4");
            typeException = TypeException.INCORRECT_DATA;
            return false;
        }

        if (hasMistakeInSpecSymbols(expression)) {
//            System.out.println("tik5");
            typeException = TypeException.INCORRECT_DATA;
            return false;

        }

        if(hasEmptyBrackets(expression)){
//            System.out.println("tik6");
            typeException = TypeException.INCORRECT_DATA;
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
                            return false;
                        }
                    }else{
                        return false;
                    }

                }
            }
        }

        return true;
    }

    private boolean hasNumberDividedZero(String expression){
//        System.out.println("it's worK?");
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
            if(partsDividedZero.size() == 0) return true;

            for(String part: partsDividedZero){
                if(!part.endsWith(",") || !part.endsWith(".")) return true;
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

        if(numberCloseBrackets == numberOpenBrackets){
            if(numberCloseBrackets == 0) return true;
            hasBrackets = true;

            return areBracketsInOrder(expression);
        }

        return false;
    }

    private boolean hasMistakeInSpecSymbols(String expression){
        if(expression.contains("1⁒(-")){
            return true;
        }
        if(expression.contains("√(-")){
            return true;
        }

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
        if(expression.contains(thirdExample) /*|| expression.contains(reverseString(thirdExample))*/) return true;
        if(expression.contains(fourthExample) /*|| expression.contains(reverseString(fourthExample))*/) return true;
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

    public enum TypeException{
        DIVIDED_ON_ZERO(),
        INCORRECT_DATA()
    }

    private boolean hasBrackets = false;
    private boolean hasSpecSymbols = false;
    private TypeException typeException;

    private String[] simplySymbols = {"+", "/", "-", "×", ",", "="};
    private String[] specSymbols = {"pow(", "√(", "1⁒(", ")", "E"};
    private String percent = "%";

    public TypeException getTypeException(){
        return typeException;
    }


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
