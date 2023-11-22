package de.drees.lang.parser.nodes;

import de.drees.lang.interpreter.CarlangContext;
import de.drees.lang.lexer.LexerPosition;
import de.drees.lang.lexer.Token;
import lombok.Getter;
import lombok.Setter;
@Getter
public class NumberNode implements ISyntaxNode {
    private final Token token;
    private final LexerPosition startPosition;
    private final LexerPosition endPosition;
    public NumberNode(Token token) {
        this.token = token;
        this.startPosition = token.getStartPosition().copy();
        this.endPosition = token.getEndPosition().copy();
    }

    @Override
    public String getStringRepresentation() {
        return String.format("(%s)", token.toStringRepresentation());
    }

    @Override
    public String getNestedRepresentation(int level) {
        return this.padLine(level) + this.getStringRepresentation();
    }

}
