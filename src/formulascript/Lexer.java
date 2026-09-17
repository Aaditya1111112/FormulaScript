package formulascript;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int pos = 0;

    public Lexer(String input) {
        this.input = input;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (Character.isWhitespace(c)) {
                pos++;
            } else if (Character.isDigit(c) || c == '.') {
                tokens.add(readNumber());
            } else if (Character.isLetter(c)) {
                tokens.add(readCell());
            } else {
                tokens.add(readSymbol(c));
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private Token readSymbol(char c) {
        pos++;
        return switch (c) {
            case '+' -> new Token(TokenType.PLUS, "+");
            case '-' -> new Token(TokenType.MINUS, "-");
            case '*' -> new Token(TokenType.MULTIPLY, "*");
            case '/' -> new Token(TokenType.DIVIDE, "/");
            case '^' -> new Token(TokenType.CARET, "^");
            case '(' -> new Token(TokenType.LPAREN, "(");
            case ')' -> new Token(TokenType.RPAREN, ")");
            case '=' -> new Token(TokenType.ASSIGN, "=");
            default -> throw new LexicalException("Unexpected character '" + c + "' at position " + (pos - 1));
        };
    }

    private Token readNumber() {
        int start = pos;
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
            pos++;
        }
        String text = input.substring(start, pos);
        return new Token(TokenType.NUMBER, text, Double.parseDouble(text));
    }

    private Token readCell() {
        int start = pos;
        while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
            pos++;
        }
        return new Token(TokenType.CELL, input.substring(start, pos).toUpperCase());
    }
}
