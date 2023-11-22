package de.drees.lang.interpreter;

import de.drees.lang.lexer.LexerPosition;
import lombok.Getter;

@Getter
public class CarlangContext {
    String displayName = "";
    CarlangContext parentContext = null;
    LexerPosition parentEntryPosition = null;

    public CarlangContext() {
        displayName = "<PROGRAM>";
    }

    public CarlangContext(String displayName, CarlangContext parentContext, LexerPosition parentEntryPosition) {
        this.displayName = displayName;
        this.parentContext = parentContext;
        this.parentEntryPosition = parentEntryPosition;
    }
}
