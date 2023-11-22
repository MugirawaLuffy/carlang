package de.drees.lang.lexer;

import lombok.Getter;

@Getter
public class Token {
    private Object payload = null;
    private final TokenType type;
    private LexerPosition startPosition, endPosition;

    public Token(TokenType type) {
        this(type, null);
    }
    public Token(TokenType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public Token(TokenType type, LexerPosition startPosition, LexerPosition endPosition) {
        this(type, null, startPosition, endPosition);
    }
    public Token(TokenType type, Object payload , LexerPosition startPosition, LexerPosition endPosition) {
        this.type = type;
        this.payload = payload;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Token(TokenType type, LexerPosition startPosition) {
        this(type, null, startPosition);
    }
    public Token(TokenType type, Object payload , LexerPosition startPosition) {
        this.type = type;
        this.payload = payload;
        this.startPosition = startPosition;
        this.endPosition = startPosition.copy().advance();
    }

    public boolean isBinaryOperator() {
        return switch (this.type) {
            case PLUS, DIVIDE, MINUS, MODULO, MULTIPLY, POWER -> true;
            default -> false;
        };
    }

    public boolean isExpressionOperator() { // + -
        return switch (this.type) {
            case PLUS, MINUS -> true;
            default -> false;
        };
    }

    public boolean isTermOperator() { // * / %
        return switch (this.type) {
            case DIVIDE, MODULO, MULTIPLY -> true;
            default -> false;
        };
    }

    public String toStringRepresentation() {
        return payload == null ? type.label : String.format("%s: %s", type.label, payload.toString());
    }
}
