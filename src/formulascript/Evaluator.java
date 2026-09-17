package formulascript;

public class Evaluator {
    private final SymbolTable symbolTable;

    public Evaluator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public double evaluate(Node node) {
        if (node instanceof Node.NumberNode n) return n.value;
        if (node instanceof Node.CellNode c) return symbolTable.get(c.name);
        if (node instanceof Node.BinOpNode b) {
            double left = evaluate(b.left);
            double right = evaluate(b.right);
            return switch (b.op) {
                case "+" -> left + right;
                case "-" -> left - right;
                case "*" -> left * right;
                case "/" -> {
                    if (right == 0) throw new RuntimeErrorException("Division by zero");
                    yield left / right;
                }
                case "^" -> Math.pow(left, right);
                default -> throw new IllegalStateException("Unknown operator " + b.op);
            };
        }
        throw new IllegalStateException("Unknown node type");
    }
}
