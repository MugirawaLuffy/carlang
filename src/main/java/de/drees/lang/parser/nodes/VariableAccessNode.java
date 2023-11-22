package de.drees.lang.parser.nodes;

import de.drees.lang.lexer.LexerPosition;
import de.drees.lang.lexer.Token;
import lombok.Getter;

@Getter
public class VariableAccessNode implements ISyntaxNode {
    Token variableName;
    private final LexerPosition startPosition;
    private final LexerPosition endPosition;
    public VariableAccessNode(Token variableName) {
        this.variableName = variableName;
        this.startPosition = variableName.getStartPosition();
        this.endPosition = variableName.getEndPosition();
    }

    @Override
    public String getStringRepresentation() {
        return null;
    }

    @Override
    public String getNestedRepresentation(int level) {
        return null;
    }

    public String getVariableNameString() {
        return this.variableName.getPayload().toString();
    }
}
