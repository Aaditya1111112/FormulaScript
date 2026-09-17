package formulascript;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Assignment parseAssignment() {
        Token cell = expect(TokenType.CELL, "Expected a cell reference on the left of '='");
        expect(TokenType.ASSIGN, "Expected '='");
        Node expr = parseExpression();
        expect(TokenType.EOF, "Unexpected token after end of formula");
        return new Assignment(cell.text, expr);
    }

    public Node parseExpressionOnly() {
        Node expr = parseExpression();
        expect(TokenType.EOF, "Unexpected token after end of formula");
        return expr;
    }

    private Node parseExpression() {
        Node left = parseTerm();
        while (peek().type == TokenType.PLUS || peek().type == TokenType.MINUS) {
            String op = advance().text;
            Node right = parseTerm();
            left = new Node.BinOpNode(op, left, right);
        }
        return left;
    }

    private Node parseTerm() {
        Node left = parsePower();
        while (peek().type == TokenType.MULTIPLY || peek().type == TokenType.DIVIDE) {
            String op = advance().text;
            Node right = parsePower();
            left = new Node.BinOpNode(op, left, right);
        }
        return left;
    }

    private Node parsePower() {
        Node base = parseFactor();
        if (peek().type == TokenType.CARET) {
            advance();
            Node exponent = parsePower();
            return new Node.BinOpNode("^", base, exponent);
        }
        return base;
    }

    private Node parseFactor() {
        Token t = peek();
        return switch (t.type) {
            case NUMBER -> { advance(); yield new Node.NumberNode(t.numberValue); }
            case CELL -> { advance(); yield new Node.CellNode(t.text); }
            case LPAREN -> {
                advance();
                Node expr = parseExpression();
                expect(TokenType.RPAREN, "Expected ')'");
                yield expr;
            }
            default -> throw new SyntaxErrorException("Unexpected '" + t.text + "'");
        };
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token advance() {
        return tokens.get(pos++);
    }

    private Token expect(TokenType type, String message) {
        if (peek().type != type) {
            throw new SyntaxErrorException(message + ", found '" + peek().text + "'");
        }
        return advance();
    }
}
