package formulascript;

public class SemanticAnalyzer {
    private final SymbolTable symbolTable;

    public SemanticAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void analyze(Node node) {
        if (node instanceof Node.CellNode cellNode) {
            if (!symbolTable.exists(cellNode.name)) {
                throw new SemanticException("Undefined cell reference " + cellNode.name);
            }
        } else if (node instanceof Node.BinOpNode binOp) {
            analyze(binOp.left);
            analyze(binOp.right);
        }
    }
}
