package working_expressions.test;

public class TestMainCalculator {
    // symbols: + - / ×
    // spec-symbols: pow( √( 1⁒( )
    // phantom-symbols: %

    public static void main(String[] args) {
        TestMainCalculator testMain = new TestMainCalculator();
        TestCalculator testCalculator = new TestCalculator();
        System.out.print("18% of '60+20' = " );
        System.out.println(testCalculator.getAnswerNumberPercentOfExpression("60+20", "18"));

//        testMain.gettingAllAnswerExpressions();
//        TestCalculator testCalculator = new TestCalculator("1⁒(2-2)", false, false);
//        System.out.println("1⁒(2-2) = " + testCalculator.getAnswerExpression());
        //        test checking
//        testMain.checkingArrayExpressions();
//        testMain.checkingArrayNCExpressions();
    }

    private void gettingAllAnswerExpressions(){
        TestCalculator testCalculator = new TestCalculator();
        for(String element: expressions){
            testCalculator.setExpression(element);
            System.out.println(element + " = " + testCalculator.getAnswerExpression());
        }
    }

    private void checkingArrayExpressions(){
        for(String element: expressions){
            System.out.println(check.isCorrectExpression(element) + "\r\n");
        }
        System.out.println();
        System.out.println("-----------------------------------------------");
    }

    private void checkingArrayNCExpressions(){
        for(String element: notCorrectExpressions){
            System.out.println(check.isCorrectExpression(element) + "\r\n");
        }
        System.out.println();
        System.out.println("-----------------------------------------------");
    }


    // all most be 'true' (through TestCheckingOnCorrect)
    private String[] expressions = {"2+3+1+5", "20+-3--2", "2+3×23/0,3+4×2", "pow(4)+3×2", "50+√(9)×2",
            "1⁒(5)×pow(2)+√(9)","1⁒(pow(√(9)))", "√(pow(1⁒(10)))×2+10/pow(2)",
            "√(pow(2×10+56×1))+pow(4+1⁒(5))+10", "50", "000", "1⁒(2-2)"};

    // all most be 'false' (through TestCheckingOnCorrect)
    private String[] notCorrectExpressions = {"rtd", "2++3-×4/-2", "pow(30))+1⁒((2)", "pow(30))+1⁒((2))","23,,,2+3",
            "pow(20)1⁒(2)", "3/0+3", "2+++5/1", "pow(√(1⁒()))"};

    private TestCheckingOnCorrect check = new TestCheckingOnCorrect();
}
