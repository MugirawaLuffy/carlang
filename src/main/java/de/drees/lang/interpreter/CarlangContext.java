package de.drees.lang.interpreter;

import de.drees.lang.lexer.LexerPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CarlangContext {
    @Setter
    SymbolTable symbolTable;
    String displayName = "";
    CarlangContext parentContext = null;
    LexerPosition parentEntryPosition = null;

    public CarlangContext() {
        displayName = "<PROGRAM>";
        symbolTable = null;
    }

    public CarlangContext(String displayName, CarlangContext parentContext, LexerPosition parentEntryPosition) {
        this.displayName = displayName;
        this.parentContext = parentContext;
        this.parentEntryPosition = parentEntryPosition;
    }
}
