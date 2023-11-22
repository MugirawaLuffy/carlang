package de.drees.lang.parser.nodes;

import de.drees.lang.lexer.Keyword;
import de.drees.lang.lexer.LexerPosition;
import de.drees.lang.lexer.Token;
import lombok.Getter;

@Getter
public class VariableAssignmentNode implements ISyntaxNode {
    private final LexerPosition startPosition;
    private final LexerPosition endPosition;
    Token variableName;
    ISyntaxNode valueNode;

    public VariableAssignmentNode(Token variableName, ISyntaxNode valueNode) {
        this.variableName = variableName;
        this.valueNode = valueNode;
        this.startPosition = variableName.getStartPosition();
        this.endPosition = valueNode.getEndPosition();
    }

    @Override
    public String getStringRepresentation() {
        return String.format("(%s, %s, %s)",
                Keyword.DECLARE.label,
                variableName,
                valueNode);
    }

    @Override
    public String getNestedRepresentation(int level) {
        return this.padLine(level) + Keyword.DECLARE.label + ": " +  variableName.getPayload().toString()
                + "\n" + valueNode.getNestedRepresentation(level+1) + "\n"
                + this.padLineSpacesOnly(level);
    }

    public String getVariableNameString() {
        return this.variableName.getPayload().toString();
    }
}
