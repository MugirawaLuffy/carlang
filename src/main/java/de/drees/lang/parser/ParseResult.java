package de.drees.lang.parser;

import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.lexer.Token;
import de.drees.lang.parser.nodes.ISyntaxNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ParseResult {
    private ISyntaxNode node;
    private CarlangException error = null;

    public ISyntaxNode register(ParseResult result) {
        if(isError()) {
            //System.out.println("Got An Error while registering");
            this.error = result.getError();
        }
        return result.getNode();
    }



    public ParseResult failure(CarlangException error) {
        //System.out.println("Got An Error while parsing: " + error.getMessage());
        this.error = error;
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
