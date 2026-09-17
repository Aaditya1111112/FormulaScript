package formulascript;

import java.util.ArrayList;
import java.util.List;

public class TACGenerator {
    private int tempCount = 0;
    private final List<String> instructions = new ArrayList<>();

    public List<String> generate(String target, Node node) {
        instructions.clear();
        tempCount = 0;
        String result = gen(node);
        instructions.add(target + " = " + result);
        return instructions;
    }

    private String gen(Node node) {
        if (node instanceof Node.NumberNode n) return Formatting.number(n.value);
        if (node instanceof Node.CellNode c) return c.name;
        if (node instanceof Node.BinOpNode b) {
            String left = gen(b.left);
            String right = gen(b.right);
            String temp = "t" + (++tempCount);
            instructions.add(temp + " = " + left + " " + b.op + " " + right);
            return temp;
        }
        throw new IllegalStateException("Unknown node type");
    }
}
