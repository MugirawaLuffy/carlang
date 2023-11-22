package de.drees.lang.parser;

import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.lexer.Token;
import de.drees.lang.parser.nodes.ISyntaxNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ParseResult {
    private int advancements = 0;
    private ISyntaxNode node;
    private CarlangException error = null;

    public ISyntaxNode register(ParseResult result) {
        this.advancements += result.getAdvancements();
        if(isError()) {
            //System.out.println("Got An Error while registering");
            this.error = result.getError();
        }
        return result.getNode();
    }

    public void registerAdvancement() {
        advancements++;
    }

    public ParseResult failure(CarlangException error) {
        if(this.error == null || advancements == 0) this.error = error;
        return this;
    }

    public ParseResult success(ISyntaxNode node) {
        this.node = node;
        return this;
    }

    public boolean isError() {
        return this.error != null;
    }
}
