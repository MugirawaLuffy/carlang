package de.drees.lang.lexer;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LexerPosition {
    private int index;
    private int lineNumber;
    private int columnNumber;

    private String fileName;
    private String fileString;

    public LexerPosition(int index, int lineNumber, int columnNumber, String fileName, String fileString) {
        this.index = index;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.fileName = fileName;
        this.fileString = fileString;
    }

    public static LexerPosition empty() {
        return new LexerPosition(-1, -1, -1, "", "");
    }

    public LexerPosition advance(Character currentCharacter) {
        index++;
        columnNumber++;

        if(currentCharacter!= null && currentCharacter == '\n') {
            lineNumber++;
            columnNumber = 0;
        }

        return this;
    }
    public LexerPosition advance() {
        index++;
        columnNumber++;
        return this;
    }

    public LexerPosition copy() {
        return new LexerPosition(this.index, this.lineNumber, this.columnNumber, this.fileName, this.fileString);
    }
}

