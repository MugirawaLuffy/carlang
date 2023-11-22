package de.drees.lang.parser.nodes;

import de.drees.lang.lexer.LexerPosition;

public interface ISyntaxNode {
    public String getStringRepresentation();
    public String getNestedRepresentation(int level);

    public LexerPosition getStartPosition();
    public LexerPosition getEndPosition();
    public default String padLine(int level) {
        int border = 0;
        int indentation = 2;
        StringBuilder padding  = new StringBuilder();
        padding.append(" ".repeat(border));
        for(int i = 0; i < level; i++) {
            padding.append("|");
            if(i == level-1) {
                padding.append("_".repeat(indentation));
            } else {
                padding.append(" ".repeat(indentation));
            }
        }
        return padding.toString();
    }
    public default String padLineSpacesOnly(int level) {
        int border = 0;
        int indentation = 2;
        StringBuilder padding  = new StringBuilder();
        padding.append(" ".repeat(border));
        for(int i = 0; i < level; i++) {
            padding.append("|");
            padding.append(" ".repeat(indentation));
        }
        return padding.toString();
    }
}
