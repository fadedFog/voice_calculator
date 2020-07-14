package application;

import calculator.UtilClass;
import calculator.checking_expressions.CheckExpression;
import calculator.core.Calculator;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("View is now loaded!");
    }

    // on Keyword Action
    //-----------------LOGIC--------------------------------//

    //----------------------------------------------------//

    // on PhotoClass and RecordClass
    //-----------------LOGIC--------------------------------//

    //----------------------------------------------------//

    private boolean isNewNumber = false;
    private boolean clearLabel = false;
    private boolean wasError = false;

    public void pressedZero(){
        checkingNewNumber();

        if(!isEqualPressed){
            if (canReplace) {
                isODPressed = isPowPressed = isSqrtPressed = isPercentPressed = false;
                canReplace = false;
                clearTextField();
                updateInputField();
                cuttingLabelLastVar();
            }
        }else{
            setDefaultVarBooleans();
            clearLabelExpression();
            clearTextField();
            clearInputField();
            isEqualPressed = false;
        }

        if(!isFirstPositionZero()) {
            addingInTextField("0");
            updateInputField();
        }
    }
    private boolean isFirstPositionZero(){
        updateTextField();

        String tmp = textField.toString();
        if(tmp.length() == 1 && tmp.equals("0")){
            return true;
        }
        return false;
    }

    public void pressedOne(){
        eventAllNumbers("1");
    }

    public void pressedTwo(){
        eventAllNumbers("2");
    }

    public void pressedThree(){
        eventAllNumbers("3");
    }

    public void pressedFour(){
        eventAllNumbers("4");
    }

    public void pressedFive(){
        eventAllNumbers("5");
    }

    public void pressedSix(){
        eventAllNumbers("6");
    }

    public void pressedSeven(){
        eventAllNumbers("7");
    }

    public void pressedEight(){
        eventAllNumbers("8");
    }

    public void pressedNine(){
        eventAllNumbers("9");
    }

    public void eventAllNumbers(String number){
        checkingNewNumber();

        if(isFirstPositionZero()) {
            clearTextField();
        }

        if(!isEqualPressed) {
            System.out.println();

            if (canReplace) {
                System.out.println("canReplace");
                isODPressed = isPowPressed = isSqrtPressed = isPercentPressed = false;
                canReplace = false;
                clearTextField();
                updateInputField();
                cuttingLabelLastVar();
            }

            if (wasPressedSpecSymbols()) {
                System.out.println("TIK2");
                clearLabel = true;
                if(isPercentPressed) {
                    clearLabel = false;
                }
                isODPressed = isPowPressed = isSqrtPressed = isPercentPressed = false;
                clearTextFieldAndInput();
                pressedEqual();
                clearTextFieldAndInput();
                clearLabelExpression();

                updateInputField();
                cuttingLabelLastVar();
                System.out.println();
            }
        }else{
            setDefaultVarBooleans();
            clearLabelExpression();
            clearTextField();
            clearInputField();
            isEqualPressed = false;
        }


        addingInTextField(number);
        updateInputField();
        wasError = false;
    }

    private void cuttingLabelLastVar(){
        updateTextLabel();
        String label = textLabel.toString();
        int toCut = 0;

        for(int i = label.length()-1; i >= 0; i--){
            char chr = label.charAt(i);
            if(isSimpleSymbol(chr)){
                toCut = i+1;
                i = 0;
            }
        }

        if(toCut != 0){
            textLabel = new StringBuilder(label.substring(0, toCut));
            updateLabelExpression();
        }
    }
    private boolean isSimpleSymbol(char chr){
        if(chr == '+' || chr == '-' || chr == '/' || chr == '×') return true;
        return false;
    }

    private boolean wasPressedSpecSymbols(){
//        System.out.println("pow " + isPowPressed);
//        System.out.println("sqrt " + isSqrtPressed);
//        System.out.println("od " + isODPressed);
//        System.out.println("percent " + isPercentPressed);
//        System.out.println("CHECK WORKING wasPressedSpecSymbols " + (isPowPressed || isSqrtPressed || isODPressed || isPercentPressed));
//        System.out.println("CHECK WORKING wasPressedSpecSymbols() " + ((isPowPressed || isSqrtPressed) || (isODPressed || isPercentPressed)));

        return isPowPressed || isSqrtPressed || isODPressed || isPercentPressed;
    }

    private boolean isPointAvailable = true;
    public void pressedPoint(){
        updateTextField();
        if(isPointAvailable && !textField.toString().contains(",")){
            if(!isSignPressed){
                textField.append(",");
            }else{
                textField = new StringBuilder("0");
                updateInputField();
                textField.append(",");
            }
            isPointAvailable = false;
        }
        updateInputField();
    }

    private boolean isSignPressed = false;
    public void pressedPlus(){
        eventsAllSimplySymbols("+");
    }

    public void pressedMinus(){
        eventsAllSimplySymbols("-");
    }

    public void pressedMultiply(){
        eventsAllSimplySymbols("×");
    }

    public void pressedDivided(){
        eventsAllSimplySymbols("/");
    }

    private void eventsAllSimplySymbols(String sSymbol){
        isNewNumber = true;
        canReplace = false;
        updateTextField();
        updateTextLabel();

        trimNeedlessZeros();
        if(!wasError) {
            if (hasLabelOnlyZero()) {
                clearLabelExpression();
            }

            if (isEqualPressed || wasPickFromList) {
                setDefaultVarBooleans();
                isNewNumber = true;
                clearLabelExpression();
            }

            if (clearLabel) {
                clearLabel = false;
                clearLabelExpression();
            }

            if (!isSignPressed) {
                if (wasPressedSpecSymbols()) {
                    addingSignInTextLabel(sSymbol);
                } else {
                    addingInTextLabel(sSymbol);
                }

                updateLabelExpression();
                isSqrtPressed = isPowPressed = isODPressed = isPercentPressed = false;
                isPointAvailable = true;
                isSignPressed = true;

            } else {
                replaceLastSign(sSymbol);
            }
        }else if(wasError && isEqualPressed){
            wasError = false;
            isEqualPressed = false;
            clearTextFieldAndInput();
            pressedZero();
            addingInTextLabel(sSymbol);
            updateLabelExpression();
        }
    }

    private boolean hasLabelOnlyZero(){
        String tmp = textLabel.toString();
        if(tmp.length() == 1 && tmp.equals("0")){
            return true;
        }
        return false;
    }

    private boolean isEqualPressed = false;
    public void pressedEqual(){
//        System.out.println("Button Equal has been pressed");
        updateTextLabel();
        updateTextField();
        if(!isEqualPressed) {

            if (!wasPressedSpecSymbols()) {
                textLabel.append(textField);
                calculator.setExpression(UtilClass.editMinuses(textLabel.toString()));
                System.out.println("IFtextLabel: " + textLabel.toString());
                System.out.println("edit textLabel: " + UtilClass.editMinuses(textLabel.toString()));
                textLabel.append("=");
            } else {
                calculator.setExpression(UtilClass.editMinuses(textLabel.toString()));
                System.out.println("ELSEtextLabel: " + textLabel.toString());
                System.out.println("edit textLabel: " + UtilClass.editMinuses(textLabel.toString()));
                textLabel.append("=");
            }

            String answer;

            if(checkExpression.isCorrectExpression(textLabel.toString())){
                System.out.println("Correct");
                answer = calculator.getAnswerExpression();
                textField = new StringBuilder(answer);

                setDefaultVarBooleans();
                addingInListMemory(textLabel.toString() + textField.toString());
            }else {
                System.out.println("InCorrect");
                wasError = true;
                answer = "ERROR";

                if(checkExpression.getTypeException().equals(CheckExpression.TypeException.DIVIDED_ON_ZERO)){
                    answer = "Деление на 0 невозможно!";
                }else if(checkExpression.getTypeException().equals(CheckExpression.TypeException.INCORRECT_DATA)){
                    answer = "Введены неверные данные!";
                }
                textField = new StringBuilder(answer);

                setDefaultVarBooleans();
            }
        }else {
            String lastSymbolWithDigit = getLastSymbolWithDigit();
            String preAnswer = textField.toString();
            String resultExpression = preAnswer + lastSymbolWithDigit;
            System.out.println("resultExpression " + resultExpression);
            calculator.setExpression(resultExpression);
            String answer = calculator.getAnswerExpression();

            clearTextField();
            clearLabelExpression();

            textField.append(answer);
            textLabel.append(preAnswer + lastSymbolWithDigit + "=");

            addingInListMemory(textLabel.toString() + textField.toString());
        }

        isEqualPressed = true;
        updateLabelExpression();
        updateInputField();
    }

    private String getLastSymbolWithDigit(){
        String label = textLabel.toString();
        boolean isStart = false;
        StringBuilder result = new StringBuilder();

        for(int i = label.length()-1; i >= 0; i--){
            char chr = label.charAt(i);

            if(isStart){
                if (isSimpleSymbol(chr)) {
                    i = 0;
                }
                result.append(chr);
            }

            if(chr == '='){
                isStart = true;
            }
        }

        result.reverse();
        return result.toString();
    }

    private boolean isPercentPressed = false;
    public void pressedPercent(){
        isSignPressed = false;
        updateTextLabel();
        updateTextField();

        trimNeedlessZeros();
        String expressionBeforePercent;
        if(!isPercentPressed) {
//            System.out.print("isPercentPressed if1");
            if(isODPressed || isSqrtPressed || isPowPressed) {
//                System.out.println(": (1)");
                expressionBeforePercent = getExpressionBeforeNumberPercent();
            }else {
//                System.out.println(": (2)");
                expressionBeforePercent = getExpressionBeforePercent();
            }
        }else {
//            System.out.println("isPercentPressed if2");
            expressionBeforePercent = getExpressionBeforeNumberPercent(1);
        }

        System.out.println("expressionBeforePercent: " + expressionBeforePercent);


        if(hasMoreThanOneDigit(expressionBeforePercent)) {
            String numberOfPercent;
            if(!isPercentPressed) {
                if(isODPressed || isSqrtPressed || isPowPressed){
//                    System.out.println("First1 if");
                    numberOfPercent = getOnlyPercentNumber();
                }else {
//                    System.out.println("First2 if");
                    numberOfPercent = textField.toString();
                }
            }else {
//                System.out.println("Second if");
                numberOfPercent = getOnlyPercentNumber();
            }

//            System.out.println("numberOfPercent FINAL: " + numberOfPercent);
//            System.out.println("has spec-symbols: " +
//                    (numberOfPercent.contains("pow") || numberOfPercent.contains("√") || numberOfPercent.contains("1⁒")));

            String resultPercentOfNumber = calculator
                    .getAnswerNumberPercentOfExpression(expressionBeforePercent, numberOfPercent);
//            System.out.println("resultPercentOfNumber: " + resultPercentOfNumber);

            if(!isPercentPressed) {
                if(isODPressed || isSqrtPressed || isPowPressed){
                    isODPressed = isSqrtPressed =  isPowPressed = false;
                    String newLabel = getExpressionBeforeNumberPercent(0) + resultPercentOfNumber;
                    clearLabelExpression();
                    textLabel.append(newLabel);
                }else {
                    textLabel.append(resultPercentOfNumber);
                }
            }else {
                String newLabel = getExpressionBeforeNumberPercent(0) + resultPercentOfNumber;
                clearLabelExpression();
                textLabel.append(newLabel);
            }
            clearTextFieldAndInput();
            textField.append(resultPercentOfNumber);
        }else{
            textLabel = new StringBuilder("0");
            textField = new StringBuilder("0");
            setDefaultVarBooleans();
            clearLabel = true;
        }
        updateInputField();
        updateLabelExpression();
        isPercentPressed = true;
    }

    private String getExpressionBeforeNumberPercent(int cut){
        int to = 0;
        for(int i = textLabel.toString().length()-1; i >= 0; i--){
            char chr = textLabel.toString().charAt(i);
            if(isSimpleSymbol(chr)){
                to = i+1;
                i = 0;
            }
        }

        String result = textLabel.toString().substring(0, to-cut);
        return result;
    }
    private String getExpressionBeforeNumberPercent(){
//        String label = textLabel.toString();
        String label = textLabel.toString();
        label = label.replaceAll("pow", "");
        label = label.replaceAll("√", "");
        label = label.replaceAll("1⁒", "");
        System.out.println("label in getExpressionBeforeNumberPercent: " + label);
        int from = 0;
        boolean wasBracket = false;

        for(int i = label.length()-1; i >= 0; i--){
            char chr = label.charAt(i);
            if(isSimpleSymbol(chr)){
                from = i;
                i = 0;
            }

            if(chr == '('){
//                System.out.print("bracket");
                if(!wasBracket){
//                    System.out.println(" start");
                    from = i;
                    wasBracket = true;
                }else{
                    i = 0;
//                    System.out.println(" continue.");
                }
            }else{
//                System.out.println("is'not bracket");
                wasBracket = false;
            }
        }
        System.out.println("labelSub: " + label.substring(0, from));
        return UtilClass.cloneString(label.substring(0, from));
    }
    private String getExpressionBeforePercent(){
        String expression = textLabel.toString();;
        if(expression.length() == 0) {
            expression = "null";
        }
        return UtilClass.cloneString(expression.substring(0, expression.length()-1));
    }
    private String getOnlyPercentNumber(){
        StringBuilder number = new StringBuilder();

        for(int i = textLabel.length()-1; i >= 0; i--){
            char chr = textLabel.toString().charAt(i);
            if(!isSimpleSymbol(chr)){
                number.append(chr);
            }else{
                i = 0;
            }
        }
        number.reverse();

        return number.toString();
    }

    private boolean hasMoreThanOneDigit(String expression){
        String expressionWithoutPoints = UtilClass.cloneString(expression.replaceAll(",", ""));
        System.out.println("expressionWithoutPoints: " + expressionWithoutPoints);
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(expressionWithoutPoints);
        List<String> digits = new ArrayList<>();

        while (matcher.find()){
            digits.add(expressionWithoutPoints.substring(matcher.start(), matcher.end()));
        }

        System.out.println("digits " + digits.toString());
//        System.exit(1);

        if(digits.size() > 0){
            return true;
        }

        return false;
    }

    private boolean canReplace = false;
    private boolean isPowPressed = false;
    public void pressedPow(){
        System.out.println("PRESSED POW");
        isSignPressed = false;
//        canReplace = true;
        updateTextLabel();
        updateTextField();

        trimNeedlessZeros();

        if(isEqualPressed){
            canReplace = true;
            clearLabel = true;
            isEqualPressed = false;
        }

        if(clearLabel){
            clearLabel = false;
            clearLabelExpression();
        }

        if(isPowPressed || isSqrtPressed || isODPressed){
            System.out.println("ADDING POW PART");
            isLastSpecialSign("pow(");
        }else{
            System.out.println("SET POW PART");
            String tmp = textField.toString();
            tmp = "pow(" + tmp + ")";
            textLabel.append(tmp);
            isPowPressed = true;
        }

        updateInputField();
        updateLabelExpression();
    }

    private boolean isSqrtPressed = false;
    public void pressedSqrt(){
        System.out.println("PRESSED SQRT");
        isSignPressed = false;
//        canReplace = true;
        updateTextLabel();
        updateTextField();

        trimNeedlessZeros();

        if(isEqualPressed){
            canReplace = true;
            clearLabel = true;
            isEqualPressed = false;
        }

        if(clearLabel){
            clearLabel = false;
            clearLabelExpression();
        }

        if(isSqrtPressed || isPowPressed || isODPressed){
            System.out.println("ADDING SQRT PART");
            isLastSpecialSign("√(");
        }else{
            System.out.println("SET SQRT PART");
            String tmp = textField.toString();
            tmp = "√(" + tmp + ")";
            textLabel.append(tmp);
            isSqrtPressed = true;
        }

        updateInputField();
        updateLabelExpression();
    }

    private boolean isODPressed = false;
    public void pressedOneDivided(){
        System.out.println("PRESSED OD");
        isSignPressed = false;
//        canReplace = true;
        updateTextLabel();
        updateTextField();

        trimNeedlessZeros();

        if(isEqualPressed){
            canReplace = true;
            clearLabel = true;
            isEqualPressed = false;
        }

        if(clearLabel){
            clearLabel = false;
            clearLabelExpression();
        }

        if(isODPressed || isSqrtPressed || isPowPressed){
            System.out.println("ADDING OD PART");
            isLastSpecialSign("1⁒(");
        }else{
            System.out.println("SET OD PART");
            String tmp = textField.toString();
            tmp = "1⁒(" + tmp + ")";
            textLabel.append(tmp);
            isODPressed = true;
        }

        updateInputField();
        updateLabelExpression();
    }

    private boolean isLastSpecialSign(String addPath){
        boolean answer = false;

        ArrayList<String> startPath = new ArrayList<String>();
        startPath.add("pow("); startPath.add("√("); startPath.add("1⁒(");

        String label = textLabel.toString();
        System.out.println("label: " + label);
        String[] signs = arrayLabelSigns();
        String[] arguments = label.split("[+×/\\-]");
        int lastId = arguments.length-1;
        System.out.println("arguments " + Arrays.toString(arguments));
        for(int i = 0; i < startPath.size(); i++){
            if(arguments[lastId].startsWith(startPath.get(i))){
                answer = true;
            }
        }

        if(answer){
            String arg = arguments[lastId];
            arg = addPath + arg + ")";
            arguments[lastId] = arg;

            StringBuilder newLabel = new StringBuilder(arguments[0]);
            for(int i = 0; i < signs.length; i++){
                newLabel.append(signs[i] + arguments[i+1]);
            }

            clearLabelExpression();
            addingTextLabelWithoutTmp(newLabel.toString());
        }
        System.out.println(answer);
        return answer;
    }
    private String[] arrayLabelSigns(){
        Pattern pattern = Pattern.compile("[+×/\\-]");
        Matcher matcher = pattern.matcher(textLabel);
        List<String> signList = new ArrayList<>();

        while (matcher.find()) signList.add(textLabel.substring(matcher.start(), matcher.end()));
        String[] signsArray = new String[signList.size()];
        for(int i = 0; i < signList.size(); i++)
            signsArray[i] = signList.get(i);

        return signsArray;
    }

    public void pressedPlusMinus(){
        if(!isFirstPositionZero()) {
            if (isStartingByMinus()) {
                String tmp = textField.toString();
                textField = new StringBuilder(tmp.substring(1));
            } else {
                addingInTextField("-", true);
            }
            updateInputField();
        }
    }
    private boolean isStartingByMinus(){
        updateInputField();
        if(textField.toString().startsWith("-")) return true;
        return false;
    }

    public void pressedBackspace(){
        updateTextField();
        backSpace();
        updateInputField();
    }

    public void pressedCE(){
        if(isEqualPressed){
            pressedClear();
        }else {
            clearTextField();
            clearInputField();
            pressedZero();
        }
        setDefaultVarBooleans();
    }

    public void pressedClear(){
        setDefaultVarBooleans();

        pressedCE();
        clearLabelExpression();
        pressedZero();
    }

    private void setDefaultVarBooleans(){
        wasError = false;
        canReplace = false;
        isNewNumber = false;
        clearLabel = false;
        wasPickFromList = false;
        isPointAvailable = true;
        isSignPressed = false;
        isEqualPressed = false;
        isODPressed = false;
        isPowPressed = false;
        isSqrtPressed = false;
        isSignPressed = false;
        isPercentPressed = false;
    }

    private void trimNeedlessZeros(){
        String newValue = textField.toString();

        if(newValue.contains(",")){
            int idLast = newValue.indexOf(',');
            int copyIdLast = idLast;
            int i = idLast + 1;

            for(; i < newValue.length(); i++){
                if(newValue.charAt(i) != '0'){
                    idLast = i;
                }
            }

            if(copyIdLast != idLast) {
                idLast += 1;
            }
            textField = new StringBuilder(newValue.substring(0, idLast));
            updateInputField();
        }

    }

    private void updateTextField(){
        textField = new StringBuilder(input_field.getText());
    }

    private void addingInTextField(String path){
        textField.append(path);
    }

    private void addingInTextField(String path, boolean beforeOrAfter){
        String tmp = textField.toString();
        textField = new StringBuilder();
        if (beforeOrAfter){
            textField.append(path + tmp);
        }else{
            textField.append(tmp + path);
        }
    }

    private void updateInputField(){
        input_field.setText(textField.toString());
    }

    private void clearInputField(){
        input_field.setText("");
    }

    private void clearTextField(){
        textField = new StringBuilder();
    }

    private void clearTextFieldAndInput(){
        clearInputField();
        clearTextField();
    }

    private void backSpace(){
        String tmpStart = input_field.getText();
        int cutLength = tmpStart.length() - 1;

        if(!(isEqualPressed || isODPressed || isSqrtPressed || isPowPressed || isPercentPressed)) {
            if (!isFirstPositionZero()) {
                String cutTmp = tmpStart.substring(0, cutLength);

                if (cutTmp.length() == 0) {
                    cutTmp = "0";
                    setDefaultVarBooleans();
                }

                if(cutTmp.contains(",")){
                    isPointAvailable = false;
                }else{
                    isPointAvailable = true;
                }

                textField = new StringBuilder();
                textField.append(cutTmp);
            }
        }
    }

    private void checkingNewNumber(){
        if(isNewNumber){
            if(isPointAvailable){
                clearInputField();
            }
            isNewNumber = false;
            isSignPressed = false;
        }
    }


    private void updateTextLabel(){
        textLabel = new StringBuilder(label_expression.getText());
    }

    private void addingSignInTextLabel(String sign){
        textLabel.append(sign);
    }

    private void addingInTextLabel(String part){
        textLabel.append(textField + part);
    }

    private void addingTextLabelWithoutTmp(String path){
        textLabel.append(path);
    }

    private void updateLabelExpression(){
        label_expression.setText(textLabel.toString());
    }

    private void replaceLastSign(String newSign){
        String partLabel = textLabel.toString();
        String newLabel = partLabel.substring(0, partLabel.length()-1) + newSign;
        textLabel = new StringBuilder(newLabel);
        updateLabelExpression();
    }

    private void clearLabelExpression(){
        textLabel = new StringBuilder();
        label_expression.setText("");
    }

    private void addingInListMemory(String expressionAnswer){
        if(list_memory.getItems().size() != 0) {
            ListView<String> newListMemory = new ListView<String>();
            newListMemory.getItems().add(expressionAnswer);

            for(int i = 0; i < list_memory.getItems().size(); i++){
                newListMemory.getItems().add(list_memory.getItems().get(i));
            }
            list_memory.getItems().clear();

            for(int i = 0; i < newListMemory.getItems().size(); i++){
                list_memory.getItems().add(newListMemory.getItems().get(i));
            }
            newListMemory.getItems().clear();

        }else{
            list_memory.getItems().add(expressionAnswer);
        }

    }

    public void clearListMemory(){
        list_memory.getItems().clear();
    }

    public void chooseExpression(){
        clearTextFieldAndInput();
        clearLabelExpression();

        String expression = list_memory.getSelectionModel().getSelectedItem();
        setDateOnLabelAndField(expression);
    }

    private boolean wasPickFromList = false;
    private void setDateOnLabelAndField(String expression){
        if(expression != null && expression.length() != 0) {
            String[] partsExpression = expression.split("=");
            String label = partsExpression[0] + "=";
            String field = partsExpression[1];
            System.out.println(label + field);

            textLabel = new StringBuilder(label);
            textField = new StringBuilder(field);

            updateLabelExpression();
            updateInputField();
            setDefaultVarBooleans();
            wasPickFromList = true;
        }else{
            textField = new StringBuilder("0");
            updateInputField();
        }
    }


    private CheckExpression checkExpression = new CheckExpression();
    private Calculator calculator = new Calculator();

    private StringBuilder textField;
    private StringBuilder textLabel;

    public TextField input_field;
    public Label label_expression;
    public ListView<String> list_memory;
}
