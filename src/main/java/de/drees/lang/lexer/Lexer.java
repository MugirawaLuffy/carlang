package de.drees.lang.lexer;

import de.drees.lang.exceptions.CarlangException;
import de.drees.lang.exceptions.CarlangIllegalCharacterException;

import java.util.ArrayList;

public class Lexer {
    private String text; //text that will be tokenized
    private LexerPosition position; //position of analysis
    private Character currentCharacter;

    public Lexer(String fileName, String text) {
        reset(fileName, text);
    }

    public void reset(String fileName, String text) {
        this.text = text;
        this.position = new LexerPosition(-1, 0, -1, fileName, text);
    }

    private void advance() {
        try {
            this.position.advance(this.currentCharacter);
            this.currentCharacter = this.text.charAt(this.position.getIndex());
        } catch (IndexOutOfBoundsException e) {
            this.currentCharacter = null;
            //System.out.println("Lexer reached EOF");
        }
    }

    private Token makeNumber() throws CarlangException {
        LexerPosition startOfToken = this.position.copy();

        Token returnToken;
        StringBuilder bufferLiteral = new StringBuilder();
        int dotCount = 0;

        while(this.currentCharacter != null && (Character.isDigit(this.currentCharacter) || this.currentCharacter == '.')) {
            if(this.currentCharacter == '.') {
                dotCount++;
                bufferLiteral.append('.');
            } else {
                bufferLiteral.append(this.currentCharacter);
            }
            this.advance();
        }

        if(dotCount > 1) {
            throw new CarlangIllegalCharacterException(startOfToken, this.position.copy(), '.');
        } else if(dotCount == 1) { // Its a float!
            returnToken = new Token(TokenType.DOUBLE, Double.parseDouble(bufferLiteral.toString()), startOfToken, this.position);
        } else {
            returnToken = new Token(TokenType.INTEGER, Integer.parseInt(bufferLiteral.toString()), startOfToken, this.position);
        }

        return returnToken;
    }

    private Boolean charIsAllowedVariableCharacter(char c) {
        if(Character.isLetter(c)) return true;
        if(Character.isDigit(c)) return true;;
        return c == '_';
    }

    private Token makeIdentifier() throws CarlangException {
        LexerPosition startOfToken = this.position.copy();
        StringBuilder bufferIdentifier = new StringBuilder();

        while(this.currentCharacter != null && charIsAllowedVariableCharacter(this.currentCharacter)) {
            bufferIdentifier.append(this.currentCharacter);
            this.advance();
        }

        TokenType type = Keyword.identifierIsKeyword(bufferIdentifier.toString()) ?
                TokenType.KEYWORD : TokenType.IDENTIFIER;

        return new Token(type, bufferIdentifier.toString(), startOfToken, this.position);
    }

    public TokenList tokenize() throws CarlangException {
        ArrayList<Token> tokens = new ArrayList<>();
        this.advance();

        while(this.currentCharacter != null) {
            if(this.currentCharacter.equals(' ') || this.currentCharacter.equals('\t')) {
                advance();
                continue;
            } else if(Character.isDigit(this.currentCharacter)) {
                tokens.add(makeNumber());
                continue;
            } else if (Character.isLetter(this.currentCharacter)) {
                tokens.add(makeIdentifier());
                continue;
            }


            switch (currentCharacter) {
                case '+' -> {
                    tokens.add(new Token(TokenType.PLUS, this.position));
                    this.advance();
                }
                case '-' -> {
                    tokens.add(new Token(TokenType.MINUS, this.position));
                    this.advance();
                }
                case '*' -> {
                    tokens.add(new Token(TokenType.MULTIPLY, this.position));
                    this.advance();
                }
                case '/' -> {
                    tokens.add(new Token(TokenType.DIVIDE, this.position));
                    this.advance();
                }
                case '%' -> {
                    tokens.add(new Token(TokenType.MODULO, this.position));
                    this.advance();
                }
                case '^' -> {
                    tokens.add(new Token(TokenType.POWER, this.position));
                    this.advance();
                }
                case '=' -> {
                    tokens.add(new Token(TokenType.EQUALS, this.position));
                    this.advance();
                }
                case '(' -> {
                    tokens.add(new Token(TokenType.LPAREN, this.position));
                    this.advance();
                }
                case ')' -> {
                    tokens.add(new Token(TokenType.RPAREN, this.position));
                    this.advance();
                }
                default -> {
                    throw new CarlangIllegalCharacterException(position.copy(),
                            position.copy().advance(this.currentCharacter),
                            this.currentCharacter);
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, this.position));
        return new TokenList(tokens);
    }
}
