package de.drees.lang.exceptions;

import de.drees.lang.lexer.LexerPosition;

public class CarlangIllegalCharacterException extends CarlangException{
    public CarlangIllegalCharacterException(LexerPosition startPos, LexerPosition endPos, char faultyChar) {
        super(startPos, endPos, "Invalid Character Error", "Lexer did not expect to find %c".formatted(faultyChar));
    }
}
