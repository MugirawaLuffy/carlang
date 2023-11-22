package de.drees.lang.parser;

import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.exceptions.CarlangInvalidSyntaxError;
import de.drees.lang.lexer.*;
import de.drees.lang.parser.nodes.*;
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
            result.registerAdvancement();
            this.advance();
            return result.success(new NumberNode(token));
        } else if (token.getType() == TokenType.IDENTIFIER) {
            result.registerAdvancement();
            this.advance();
            return result.success(new VariableAccessNode(token));
        } else if(token.getType() == TokenType.LPAREN) {
            result.registerAdvancement();
            this.advance();
            ISyntaxNode expression = result.register(expression());
            if(result.isError()) return result;
            if(this.currentToken.getType() == TokenType.RPAREN) {
                result.registerAdvancement();
                advance();
                return result.success(expression);
            } else {
                return result.failure(CarlangInvalidSyntaxError.fromExpectation(currentToken.getStartPosition(),
                        currentToken.getEndPosition(), new char[]{')'}));
            }
        } else {
            return result.failure(new CarlangInvalidSyntaxError(token.getStartPosition(), token.getEndPosition(),
                    "Expected an identifier, a number, '+', '-' or '('"));
        }
    }

    private ParseResult factor() {
        ParseResult result = new ParseResult();
        Token token = this.currentToken;

        if(token.isExpressionOperator()) {
            result.registerAdvancement();
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
        } else if(left == null) {
            return result.failure(new CarlangInvalidSyntaxError(
                    currentToken.getStartPosition(),
                    currentToken.getEndPosition(), "No left-hand node found"));
        }

        while(relevancySupplier.get()) {
            Token operator = this.currentToken;
            result.registerAdvancement();
            this.advance();
            ISyntaxNode right = result.register(rightNodeSupplier.get());
            if(result.isError()) {
                return result;
            }else if(right == null) {
                return result.failure(new CarlangInvalidSyntaxError(
                        currentToken.getStartPosition(),
                        currentToken.getEndPosition(), "No right-hand node found"));
            }
            left = new BinaryOperationNode(left, operator, right);
        }

        return result.success(left);
    }

    private ParseResult term() { // / *
        return binaryOperation(this::factor, () -> this.currentToken != null && this.currentToken.isTermOperator());
    }
    private ParseResult expression() { // + - and variable assignments
        ParseResult result = new ParseResult();
        if(currentToken.isSpecificKeyword(Keyword.DECLARE)) { // LOOK FOR VARIABLE ASSIGNMENT
            result.registerAdvancement();
            this.advance();
            if(this.currentToken.getType() != TokenType.IDENTIFIER) { // IF ASSIGNMENT NOT FOLLOWED BY IDENTIFIER
                                                                      // THERE IS SOMETHING WRONG
                return result.failure(new CarlangInvalidSyntaxError(
                        this.currentToken.getStartPosition(),
                        this.currentToken.getEndPosition(),"Expected an identifier after assignment keyword."));
            }
            // When this part is reached, variable name has been found out and '=' has to be looked for
            Token identifier = this.currentToken;
            result.registerAdvancement();
            this.advance();

            if(this.currentToken.getType() != TokenType.EQUALS) { // If next token is not '=', there is something wrong
                return result.failure(new CarlangInvalidSyntaxError(
                        this.currentToken.getStartPosition(),
                        this.currentToken.getEndPosition(),"Expected '=' after identifier."));
            }
            result.registerAdvancement();
            this.advance();
            ISyntaxNode subExpression = result.register(expression());

            if(result.isError()) return result;
            return result.success(new VariableAssignmentNode(identifier, subExpression));
        }

        ISyntaxNode node = result.register(binaryOperation(this::term,
                () -> this.currentToken != null && this.currentToken.isExpressionOperator()));

        if(result.isError()) {
            return result.failure(new CarlangInvalidSyntaxError(this.currentToken.getStartPosition(),
                    this.currentToken.getEndPosition(),
                    "Expected '%s' an identifier, a number, '+', '-' or '('".formatted(Keyword.DECLARE.label)));
        }

        return result.success(node);
    }
}
