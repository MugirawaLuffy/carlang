package de.drees.lang.interpreter;

import de.drees.lang.lexer.LexerPosition;

public interface CarlangValue {
    public CarlangValue setPositions(LexerPosition startPosition, LexerPosition endPosition);
    public String typeInfoString();
}
