package de.drees.lang.lexer;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class TokenList {
    private ArrayList<Token> tokens;

    public TokenList(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public void printTokens() {
        StringBuilder bufferString = new StringBuilder("[ ");
        for (var token: this.tokens) {
            bufferString.append(token.toStringRepresentation());
            bufferString.append(", ");
        }
        bufferString.setCharAt(bufferString.length() - 2, ' ');
        bufferString.setCharAt(bufferString.length() - 1, ']');
        System.out.println(bufferString.toString());
    }

}
