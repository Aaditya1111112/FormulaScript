package formulascript;

public class Optimizer {

    public Node optimize(Node node) {
        if (!(node instanceof Node.BinOpNode b)) return node;

        Node left = optimize(b.left);
        Node right = optimize(b.right);

        if (left instanceof Node.NumberNode ln && right instanceof Node.NumberNode rn) {
            return new Node.NumberNode(fold(b.op, ln.value, rn.value));
        }
        if (right instanceof Node.NumberNode rn) {
            if ((b.op.equals("+") || b.op.equals("-")) && rn.value == 0) return left;
            if (b.op.equals("*") && rn.value == 1) return left;
            if (b.op.equals("*") && rn.value == 0) return new Node.NumberNode(0);
            if (b.op.equals("^") && rn.value == 1) return left;
        }
        if (left instanceof Node.NumberNode ln) {
            if (b.op.equals("+") && ln.value == 0) return right;
            if (b.op.equals("*") && ln.value == 1) return right;
            if (b.op.equals("*") && ln.value == 0) return new Node.NumberNode(0);
        }
        return new Node.BinOpNode(b.op, left, right);
    }

    private double fold(String op, double a, double b) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalStateException("Unknown operator " + op);
        };
    }
}
