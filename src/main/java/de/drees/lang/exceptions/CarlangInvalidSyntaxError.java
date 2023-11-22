package de.drees.lang.exceptions;

import de.drees.lang.lexer.LexerPosition;

public class CarlangInvalidSyntaxError extends CarlangException {
    public CarlangInvalidSyntaxError(LexerPosition startPos, LexerPosition endPos, String details) {
        super(startPos, endPos, "Invalid Syntax Error", details);
    }

    public static CarlangInvalidSyntaxError fromExpectation(LexerPosition startPos, LexerPosition endPos, char[] expected) {
        StringBuilder expectation = new StringBuilder("No more Information about the error could be obtained");
        if(expected.length == 1) {
            expectation = new StringBuilder("Expected to find '%c'".formatted(expected[0]));
        } else if(expected.length > 1) {
            expectation = new StringBuilder("Expected to find one of these tokens:");
            for(char c : expected) {
                expectation.append(" '%c',".formatted(c));
            }
            expectation.setCharAt(expectation.length()-1, '.');
        }

        return new CarlangInvalidSyntaxError(startPos, endPos, expectation.toString());
    }
}
