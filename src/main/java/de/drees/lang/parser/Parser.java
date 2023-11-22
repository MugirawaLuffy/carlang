package de.drees.lang.parser;

import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.exceptions.CarlangInvalidSyntaxError;
import de.drees.lang.lexer.Lexer;
import de.drees.lang.lexer.Token;
import de.drees.lang.lexer.TokenList;
import de.drees.lang.lexer.TokenType;
import de.drees.lang.parser.nodes.BinaryOperationNode;
import de.drees.lang.parser.nodes.ISyntaxNode;
import de.drees.lang.parser.nodes.NumberNode;
import de.drees.lang.parser.nodes.UnaryOperationNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.function.Supplier;

@Getter
public class Parser {
    ArrayList<Token> tokens;
    int tokenIndex = -1;
    Token currentToken;

    public Parser(TokenList list) {
        this.tokens = list.getTokens();
    }

    public Parser(Lexer lexer) throws CarlangException {
        this.tokens = lexer.tokenize().getTokens();
    }

    public ParseResult parse() {
        advance();
        ParseResult result = expression();
        if(!result.isError() && this.currentToken.getType() != TokenType.EOF) {
            return result.failure(CarlangInvalidSyntaxError.fromExpectation(this.currentToken.getStartPosition(),
                    this.currentToken.getEndPosition(), new char[]{'+', '-', '*', '/', '%'}));
        }
        return result;
    }

    private Token advance() {
        this.tokenIndex++;
        if(tokenIndex < tokens.size()) {
            this.currentToken = tokens.get(tokenIndex);
        }
        return this.currentToken;
    }

    private ParseResult atomic() {
        ParseResult result = new ParseResult();
        Token token = this.currentToken;

        if(token.getType() == TokenType.DOUBLE || token.getType() == TokenType.INTEGER) {
            this.advance();
            return result.success(new NumberNode(token));
        } else if(token.getType() == TokenType.LPAREN) {
            this.advance();
            ISyntaxNode expression = result.register(expression());
            if(result.isError()) return result;
            if(this.currentToken.getType() == TokenType.RPAREN) {
                advance();
                return result.success(expression);
            } else {
                return result.failure(CarlangInvalidSyntaxError.fromExpectation(currentToken.getStartPosition(),
                        currentToken.getEndPosition(), new char[]{')'}));
            }
        } else {
            return result.failure(new CarlangInvalidSyntaxError(token.getStartPosition(), token.getEndPosition(), "Expected a number, '+', '-' or '('"));
        }
    }

    private ParseResult factor() {
        ParseResult result = new ParseResult();
        Token token = this.currentToken;

        if(token.isExpressionOperator()) {
            this.advance();
            ISyntaxNode factor = result.register(factor());
            if(result.isError()) return result;
            return result.success(new UnaryOperationNode(token, factor));
        } else {
            return this.power();
        }
    }

    private ParseResult power() {
        return binaryOperation(this::atomic,
                () -> this.currentToken != null && this.currentToken.getType() == TokenType.POWER,
                this::factor);
    }

    private ParseResult binaryOperation(Supplier<ParseResult> nodeSupplier, Supplier<Boolean> relevancySupplier) {
        return binaryOperation(nodeSupplier, relevancySupplier, nodeSupplier);
    }

    private ParseResult binaryOperation(Supplier<ParseResult> leftNodeSupplier,
                                        Supplier<Boolean> relevancySupplier,
                                        Supplier<ParseResult> rightNodeSupplier) {
        ParseResult result = new ParseResult();
        ISyntaxNode left = result.register(leftNodeSupplier.get());

        if(result.isError()) {
            return result;
        }

        while(relevancySupplier.get()) {
            Token operator = this.currentToken;
            this.advance();
            ISyntaxNode right = result.register(rightNodeSupplier.get());
            if(result.isError()) {
                return result;
            }
            left = new BinaryOperationNode(left, operator, right);
        }

        return result.success(left);
    }

    private ParseResult term() { // / *
        return binaryOperation(this::factor, () -> this.currentToken != null && this.currentToken.isTermOperator());
    }
    private ParseResult expression() { // + -
        return binaryOperation(this::term, () -> this.currentToken != null && this.currentToken.isExpressionOperator());
    }
}
