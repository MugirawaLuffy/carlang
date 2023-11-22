package de.drees.lang.exceptions;

import de.drees.lang.interpreter.CarlangContext;
import de.drees.lang.lexer.LexerPosition;
import lombok.Getter;

@Getter
public class CarlangRuntimeException extends CarlangException {
    CarlangContext ctx = null;
    public CarlangRuntimeException(LexerPosition startPos, LexerPosition endPos, String details) {
        super(startPos, endPos, "Runtime Exception", details);
    }

    public CarlangRuntimeException setErrorContext(CarlangContext ctx) {
        this.ctx = ctx;
        return this;
    }

    public String generateTraceBack() {
        StringBuilder traceBackBuilder = new StringBuilder("Traceback (most recent call last):\n");

        LexerPosition traceBackPosition = this.getStartPos();
        CarlangContext displayingContext = this.ctx;

        while(displayingContext != null) {
            traceBackBuilder.append("\tFile %s, line %d, in %s\n"
                    .formatted(traceBackPosition.getFileName(),
                            traceBackPosition.getLineNumber(),
                            displayingContext.getDisplayName()));

            traceBackPosition = displayingContext.getParentEntryPosition();
            displayingContext = displayingContext.getParentContext();
        }

        return traceBackBuilder.toString();
    }
}
