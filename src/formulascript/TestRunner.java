package formulascript;

public class TestRunner {
    private static int passed;
    private static int failed;

    public static boolean runAll() {
        passed = 0;
        failed = 0;
        Interpreter in = new Interpreter();

        check("1  Assign a constant", in.execute("A1 = 10", false), "A1 = 10");
        in.execute("B1 = 20", false);
        check("2  Operator precedence", in.execute("C1 = A1 + B1 * 5", false), "C1 = 110");
        check("3  Parentheses override precedence", in.execute("D1 = (10 + 20) * 2", false), "D1 = 60");
        check("4  Right-associative exponent", in.execute("E1 = 2 ^ 3 ^ 2", false), "E1 = 512");
        check("5  Incomplete formula", in.execute("F1 = 10 +", false), "Syntax Error: Unexpected end of formula");
        check("6  Malformed expression", expressionOnly("A1 + * B2"), "Syntax Error: Unexpected '*'");
        check("7  Undefined cell", in.execute("G1 = A1 + X5", false), "Semantic Error: Undefined cell reference X5");
        check("8  Division by zero", in.execute("H1 = 10 / 0", false), "Runtime Error: Division by zero");
        check("9  Constant folding", optimizedTac("I1 = 10 + 20"), "[I1 = 30]");
        check("10 Algebraic simplification", optimizedTac("J1 = B1 + 0"), "[J1 = B1]");
        check("11 Unary minus", in.execute("K1 = -A1 + 3", false), "K1 = -7");
        check("12 Unrecognized character", in.execute("L1 = 5 $ 2", false), "Lexical Error: Unexpected character '$' at position 7");
        check("13 Division by zero is not folded", optimizedTac("M1 = 10 / 0"), "[t1 = 10 / 0, M1 = t1]");

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        return failed == 0;
    }

    private static String expressionOnly(String text) {
        try {
            new Parser(new Lexer(text).tokenize()).parseExpressionOnly();
            return "parsed";
        } catch (SyntaxErrorException e) {
            return "Syntax Error: " + e.getMessage();
        }
    }

    private static String optimizedTac(String line) {
        Assignment a = new Parser(new Lexer(line).tokenize()).parseAssignment();
        Node optimized = new Optimizer().optimize(a.expression());
        return new TACGenerator().generate(a.target(), optimized).toString();
    }

    private static void check(String name, String actual, String expected) {
        if (actual.equals(expected)) {
            passed++;
            System.out.println("PASS  " + name);
        } else {
            failed++;
            System.out.println("FAIL  " + name);
            System.out.println("      expected: " + expected);
            System.out.println("      actual:   " + actual);
        }
    }
}
