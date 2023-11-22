package de.drees.lang.parser.nodes;

import de.drees.lang.lexer.LexerPosition;
import de.drees.lang.lexer.Token;
import lombok.Getter;

@Getter
public class BinaryOperationNode implements ISyntaxNode {
    private final LexerPosition startPosition;
    private final LexerPosition endPosition;
    ISyntaxNode leftNode, rightNode;
    Token operator;

    public BinaryOperationNode(ISyntaxNode leftNode, Token operator, ISyntaxNode rightNode) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.operator = operator;
        this.startPosition = leftNode.getStartPosition().copy();
        this.endPosition = leftNode.getEndPosition().copy();
    }


    @Override
    public String getStringRepresentation() {
        return String.format("(%s, %s, %s)", leftNode.getStringRepresentation(),
                operator.toStringRepresentation(),
                rightNode.getStringRepresentation());
    }

    @Override
    public String getNestedRepresentation(int level) {
        return this.padLine(level) + this.operator.toStringRepresentation()
                + "\n" + leftNode.getNestedRepresentation(level+1)
                + "\n" + rightNode.getNestedRepresentation(level+1) + "\n"
                + this.padLineSpacesOnly(level);
    }
}
