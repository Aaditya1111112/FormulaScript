package formulascript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            runDemo();
        } else if (args[0].equals("--test")) {
            if (!TestRunner.runAll()) System.exit(1);
        } else if (args[0].equals("--repl")) {
            runRepl();
        } else {
            runFile(args[0]);
        }
    }

    private static void runFile(String path) throws IOException {
        Interpreter interpreter = new Interpreter();
        for (String line : Files.readAllLines(Path.of(path))) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
            interpreter.execute(trimmed, true);
        }
        printCells(interpreter.symbols());
    }

    private static void runRepl() {
        Interpreter interpreter = new Interpreter();
        Scanner in = new Scanner(System.in);
        System.out.println("FormulaScript REPL. Enter a formula such as A1 = 10, or 'exit' to quit.");
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) break;
            String line = in.nextLine().trim();
            if (line.equals("exit")) break;
            if (line.isEmpty()) continue;
            interpreter.execute(line, true);
        }
        printCells(interpreter.symbols());
    }

    private static void printCells(SymbolTable table) {
        header("Final cell values");
        for (Map.Entry<String, Double> e : table.asMap().entrySet()) {
            System.out.println(e.getKey() + " = " + Formatting.number(e.getValue()));
        }
    }

    private static void runDemo() {
        SymbolTable symbolTable = new SymbolTable();

        header("FormulaScript - Phase 2");

        runAssignment(symbolTable, "A1 = 10");
        runAssignment(symbolTable, "B1 = 20");
        runAssignment(symbolTable, "C1 = A1 + B1 * 5");

        header("Optimization example: A1 = 10 + 20");
        showOptimization("10 + 20");

        header("Optimization example: A1 = B1 + 0");
        symbolTable.set("B1", 20);
        showOptimization("B1 + 0");

        header("Error Demonstrations");
        demoSyntaxError();
        demoParserError();
        demoSemanticError(symbolTable);
        demoRuntimeError();
    }

    private static void runAssignment(SymbolTable symbolTable, String line) {
        System.out.println("Input: " + line);

        List<Token> tokens = new Lexer(line).tokenize();
        System.out.println("Tokens: " + tokens.subList(0, tokens.size() - 1));

        Assignment assignment = new Parser(tokens).parseAssignment();
        new SemanticAnalyzer(symbolTable).analyze(assignment.expression());

        System.out.println("AST:");
        System.out.print(new AstPrinter().print(assignment.expression()));

        List<String> tacBefore = new TACGenerator().generate(assignment.target(), assignment.expression());
        System.out.println("TAC (before optimization):");
        tacBefore.forEach(l -> System.out.println("  " + l));

        Node optimized = new Optimizer().optimize(assignment.expression());
        List<String> tacAfter = new TACGenerator().generate(assignment.target(), optimized);
        System.out.println("TAC (after optimization):");
        tacAfter.forEach(l -> System.out.println("  " + l));

        double value = new Evaluator(symbolTable).evaluate(assignment.expression());
        symbolTable.set(assignment.target(), value);
        System.out.println("Result: " + assignment.target() + " = " + Formatting.number(value));
        System.out.println();
    }

    private static void showOptimization(String expression) {
        System.out.println("Input: " + expression);
        List<Token> tokens = new Lexer(expression).tokenize();
        Node expr = new Parser(tokens).parseExpressionOnly();

        List<String> before = new TACGenerator().generate("A1", expr);
        System.out.println("Before:");
        before.forEach(l -> System.out.println("  " + l));

        Node optimized = new Optimizer().optimize(expr);
        List<String> after = new TACGenerator().generate("A1", optimized);
        System.out.println("After:");
        after.forEach(l -> System.out.println("  " + l));
        System.out.println();
    }

    private static void demoSyntaxError() {
        String line = "A1 = 10 +";
        System.out.println("Input: " + line);
        try {
            List<Token> tokens = new Lexer(line).tokenize();
            new Parser(tokens).parseAssignment();
        } catch (SyntaxErrorException e) {
            System.out.println("Syntax Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demoParserError() {
        String line = "A1 + * B2";
        System.out.println("Input: " + line);
        try {
            List<Token> tokens = new Lexer(line).tokenize();
            new Parser(tokens).parseExpressionOnly();
        } catch (SyntaxErrorException e) {
            System.out.println("Syntax Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demoSemanticError(SymbolTable symbolTable) {
        String line = "C1 = A1 + X5";
        System.out.println("Input: " + line);
        try {
            List<Token> tokens = new Lexer(line).tokenize();
            Assignment assignment = new Parser(tokens).parseAssignment();
            new SemanticAnalyzer(symbolTable).analyze(assignment.expression());
        } catch (SemanticException e) {
            System.out.println("Semantic Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void demoRuntimeError() {
        String line = "C1 = 10 / 0";
        System.out.println("Input: " + line);
        List<Token> tokens = new Lexer(line).tokenize();
        Assignment assignment = new Parser(tokens).parseAssignment();
        SymbolTable emptyTable = new SymbolTable();
        new SemanticAnalyzer(emptyTable).analyze(assignment.expression());
        try {
            new Evaluator(emptyTable).evaluate(assignment.expression());
        } catch (RuntimeErrorException e) {
            System.out.println("Runtime Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void header(String title) {
        System.out.println("=".repeat(60));
        System.out.println(title);
        System.out.println("=".repeat(60));
    }
}
