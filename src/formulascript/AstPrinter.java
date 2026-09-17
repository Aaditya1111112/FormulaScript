package formulascript;

public class AstPrinter {

    public String print(Node node) {
        StringBuilder sb = new StringBuilder();
        print(node, sb, "");
        return sb.toString();
    }

    private void print(Node node, StringBuilder sb, String prefix) {
        if (node instanceof Node.NumberNode n) {
            sb.append(prefix).append("+-- ").append(Formatting.number(n.value)).append("\n");
        } else if (node instanceof Node.CellNode c) {
            sb.append(prefix).append("+-- ").append(c.name).append("\n");
        } else if (node instanceof Node.BinOpNode b) {
            sb.append(prefix).append("+-- ").append(b.op).append("\n");
            print(b.left, sb, prefix + "|   ");
            print(b.right, sb, prefix + "    ");
        }
    }
}
