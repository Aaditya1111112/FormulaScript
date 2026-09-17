package formulascript;

public abstract class Node {

    public static final class NumberNode extends Node {
        public final double value;
        public NumberNode(double value) { this.value = value; }
    }

    public static final class CellNode extends Node {
        public final String name;
        public CellNode(String name) { this.name = name; }
    }

    public static final class BinOpNode extends Node {
        public final String op;
        public final Node left;
        public final Node right;
        public BinOpNode(String op, Node left, Node right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }
    }
}
