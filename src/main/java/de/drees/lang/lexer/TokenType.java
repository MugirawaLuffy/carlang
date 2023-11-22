package de.drees.lang.lexer;

public enum TokenType {
    INTEGER ("INT"), //number, eg 1
    DOUBLE("DOUBLE"), //floating point number, eg 1.0
    PLUS ("PLUS"), // +
    MINUS ("MINUS"), // -
    DIVIDE ("DIVIDE"), // -
    MODULO ("MODULO"), // -
    POWER ("POWER"), // -
    MULTIPLY ("MULTIPLY"), // *
    LPAREN ("L-PAREN"), // (
    RPAREN ("R-PAREN"), // )
    EQUALS ("EQUALS"), // assign operator
    KEYWORD ("KEYWORD"), // reserved words in carlang
    IDENTIFIER ("IDENTIFIER"), //represents variable names (and function names, later)

    EOF("EOF")

    ;

    public final String label;

    private TokenType(String label) {
        this.label = label;
    }
}
