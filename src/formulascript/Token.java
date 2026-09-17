package formulascript;

public class Token {
    public final TokenType type;
    public final String text;
    public final double numberValue;

    public Token(TokenType type, String text) {
        this(type, text, 0);
    }

    public Token(TokenType type, String text, double numberValue) {
        this.type = type;
        this.text = text;
        this.numberValue = numberValue;
    }

    @Override
    public String toString() {
        return switch (type) {
            case CELL -> "CELL(" + text + ")";
            case NUMBER -> "NUMBER(" + Formatting.number(numberValue) + ")";
            case PLUS -> "PLUS";
            case MINUS -> "MINUS";
            case MULTIPLY -> "MULTIPLY";
            case DIVIDE -> "DIVIDE";
            case CARET -> "CARET";
            case LPAREN -> "LPAREN";
            case RPAREN -> "RPAREN";
            case ASSIGN -> "ASSIGN";
            case EOF -> "EOF";
        };
    }
}
