package formulascript;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, Double> values = new LinkedHashMap<>();

    public boolean exists(String cell) {
        return values.containsKey(cell);
    }

    public double get(String cell) {
        if (!exists(cell)) {
            throw new SemanticException("Undefined cell reference " + cell);
        }
        return values.get(cell);
    }

    public void set(String cell, double value) {
        values.put(cell, value);
    }

    public Map<String, Double> asMap() {
        return values;
    }
}
