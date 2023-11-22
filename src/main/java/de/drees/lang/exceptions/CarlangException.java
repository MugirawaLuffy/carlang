package de.drees.lang.exceptions;

import de.drees.lang.interpreter.CarlangContext;
import de.drees.lang.lexer.LexerPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CarlangException extends Exception {
    @Setter private LexerPosition startPos;
    @Setter private LexerPosition endPos;

    public CarlangException(LexerPosition startPos, LexerPosition endPos, String errorName, String details) {
        super("[" + errorName + "] " + details);
        this.startPos = startPos;
        this.endPos = endPos;
    }
}
