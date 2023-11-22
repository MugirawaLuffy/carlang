package de.drees.lang.parser.nodes;

import de.drees.lang.lexer.LexerPosition;
import de.drees.lang.lexer.Token;
import lombok.Getter;

@Getter
public class UnaryOperationNode implements ISyntaxNode {
    private final LexerPosition startPosition;
    private final LexerPosition endPosition;
    private ISyntaxNode node;
    private Token operator;

    public UnaryOperationNode(Token operator, ISyntaxNode node) {
        this.node = node;
        this.operator = operator;
        this.startPosition = operator.getStartPosition();
        this.endPosition = operator.getEndPosition();
    }

    @Override
    public String getStringRepresentation() {
        return String.format("(%s, %s)", operator.toStringRepresentation(), node.getStringRepresentation());
    }

    @Override
    public String getNestedRepresentation(int level) {
        return this.padLine(level) + this.getStringRepresentation();
    }
}
