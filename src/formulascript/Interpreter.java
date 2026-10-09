package formulascript;

import java.util.List;

public class Interpreter {
    private final SymbolTable symbolTable = new SymbolTable();

    public SymbolTable symbols() {
        return symbolTable;
    }

    public String execute(String line, boolean verbose) {
        if (verbose) System.out.println("Input: " + line);
        boolean ok = false;
        String outcome;
        try {
            List<Token> tokens = new Lexer(line).tokenize();
            if (verbose) System.out.println("Tokens: " + tokens.subList(0, tokens.size() - 1));
            Assignment assignment = new Parser(tokens).parseAssignment();
            new SemanticAnalyzer(symbolTable).analyze(assignment.expression());
            if (verbose) showStages(assignment);
            double value = new Evaluator(symbolTable).evaluate(assignment.expression());
            symbolTable.set(assignment.target(), value);
            outcome = assignment.target() + " = " + Formatting.number(value);
            ok = true;
        } catch (LexicalException e) {
            outcome = "Lexical Error: " + e.getMessage();
        } catch (SyntaxErrorException e) {
            outcome = "Syntax Error: " + e.getMessage();
        } catch (SemanticException e) {
            outcome = "Semantic Error: " + e.getMessage();
        } catch (RuntimeErrorException e) {
            outcome = "Runtime Error: " + e.getMessage();
        }
        if (verbose) {
            System.out.println(ok ? "Result: " + outcome : outcome);
            System.out.println();
        }
        return outcome;
    }

    private void showStages(Assignment assignment) {
        System.out.println("AST:");
        System.out.print(new AstPrinter().print(assignment.expression()));

        System.out.println("TAC (before optimization):");
        new TACGenerator().generate(assignment.target(), assignment.expression())
                .forEach(l -> System.out.println("  " + l));

        Node optimized = new Optimizer().optimize(assignment.expression());
        System.out.println("TAC (after optimization):");
        new TACGenerator().generate(assignment.target(), optimized)
                .forEach(l -> System.out.println("  " + l));
    }
}
